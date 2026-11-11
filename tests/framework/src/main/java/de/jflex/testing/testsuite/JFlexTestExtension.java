/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.testsuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.jflex.testing.diff.DiffOutputStream;
import de.jflex.testing.testsuite.annotations.NoExceptionThrown;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import jflex.generator.LexGenerator;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Runs JFlex on the spec of a test class with a {@link TestSpec}, and compiles the generated
 * scanner, before the tests of the class run. {@link JFlexTest} reports the results of these steps
 * as tests. If a step fails, the other tests of the class are skipped.
 *
 * <p>{@link TestSpec} registers this extension.
 */
public class JFlexTestExtension implements BeforeAllCallback, BeforeEachCallback {

  /** The outcome of generating and compiling the scanner of a test class. */
  private static final class Build {
    /** Whether the spec expects JFlex to generate a scanner. */
    boolean expectsLexer;

    Throwable generateFailure;
    Throwable compileFailure;

    boolean failed() {
      return generateFailure != null || compileFailure != null;
    }
  }

  private static final Map<Class<?>, Build> BUILDS = new ConcurrentHashMap<>();

  @Override
  public void beforeAll(ExtensionContext context) {
    Class<?> testClass = context.getRequiredTestClass();
    if (!JFlexTest.class.isAssignableFrom(testClass)) {
      throw new IllegalStateException(
          testClass.getName() + " has a @TestSpec and must implement " + JFlexTest.class.getName());
    }
    TestSpec spec = testClass.getAnnotation(TestSpec.class);
    Build build = new Build();
    build.expectsLexer = spec.generatorThrows() == NoExceptionThrown.class;
    String lexerJavaFileName = null;
    try {
      lexerJavaFileName = generateLexer(spec);
    } catch (Throwable th) {
      build.generateFailure = th;
    }
    if (lexerJavaFileName != null) {
      try {
        JavacUtils.compile(Collections.singletonList(new File(lexerJavaFileName)));
      } catch (Throwable th) {
        build.compileFailure = th;
      }
    }
    BUILDS.put(testClass, build);
  }

  /** Skips the tests of the class if there is no scanner for them. */
  @Override
  public void beforeEach(ExtensionContext context) {
    boolean isReport = context.getRequiredTestMethod().getDeclaringClass() == JFlexTest.class;
    if (!isReport && BUILDS.get(context.getRequiredTestClass()).failed()) {
      Assumptions.abort("JFlex generation or compilation of the scanner failed");
    }
  }

  /** The results of generating and compiling the scanner of {@code testClass}, as tests. */
  static List<DynamicTest> report(Class<?> testClass) {
    Build build = BUILDS.get(testClass);
    List<DynamicTest> tests = new ArrayList<>();
    tests.add(DynamicTest.dynamicTest("Generate Lexer", () -> rethrow(build.generateFailure)));
    if (build.expectsLexer) {
      tests.add(
          DynamicTest.dynamicTest(
              "Compile java code",
              () -> {
                Assumptions.assumeTrue(
                    build.generateFailure == null, "Generating the lexer failed");
                rethrow(build.compileFailure);
              }));
    }
    return tests;
  }

  private static void rethrow(Throwable failure) throws Throwable {
    if (failure != null) {
      throw failure;
    }
  }

  /**
   * Runs JFlex on the spec and checks its output and exceptions against the spec.
   *
   * @return the generated file, or null if the spec expects JFlex to fail
   */
  private static String generateLexer(TestSpec spec) {
    Optional<DiffOutputStream> diffSysOut = injectDiffSysOut(spec);
    try {
      String lexerJavaFileName = null;
      if (spec.generatorThrows() == NoExceptionThrown.class) {
        lexerJavaFileName = invokeJflex(spec, diffSysOut);
      } else {
        generateLexerWithExpectedException(spec, diffSysOut);
      }
      diffSysOut.ifPresent(
          s ->
              assertEquals("", s.remainingContent(), "Expected content not printed to System.out"));
      return lexerJavaFileName;
    } finally {
      // The content was checked above, so close without further checks.
      diffSysOut.ifPresent(DiffOutputStream::closeWithoutCheck);
    }
  }

  private static void generateLexerWithExpectedException(
      TestSpec spec, Optional<DiffOutputStream> diffSysOut) {
    try {
      invokeJflex(spec, diffSysOut);
    } catch (Throwable e) {
      assertInstanceOf(
          spec.generatorThrows(),
          e,
          "@TestSpec indicates that the jflex generation must throw a "
              + spec.generatorThrows().getSimpleName());
      if (spec.generatorThrowableCause() == Void.class) {
        assertNull(
            e.getCause(), "@TestSpec indicates that there is no cause for the generator exception");
      } else if (spec.generatorThrowableCause() != NoExceptionThrown.class) {
        assertInstanceOf(
            spec.generatorThrowableCause(),
            e.getCause(),
            "@TestSpec indicates that cause of the generator exception is "
                + spec.generatorThrowableCause().getSimpleName());
      }
      // Expected
      return;
    }
    throw new AssertionError(
        "@TestSpec indicates that the jflex generation throws a "
            + spec.generatorThrows().getSimpleName()
            + " but nothing was thrown");
  }

  /**
   * Returns a stream that compares what JFlex prints with the golden {@code sysout} file of the
   * spec, or empty if the spec has none.
   */
  private static Optional<DiffOutputStream> injectDiffSysOut(TestSpec spec) {
    if (!spec.sysout().isEmpty()) {
      File sysoutFile = new File(spec.sysout());
      try {
        DiffOutputStream diffSysOut =
            new DiffOutputStream(
                Files.newBufferedReader(sysoutFile.toPath(), StandardCharsets.UTF_8));
        diffSysOut.setComparisonFailureHandler(
            failure -> {
              throw new Error("System.out differs: " + spec.sysout(), failure);
            });
        return Optional.of(diffSysOut);
      } catch (IOException e) {
        throw new AssertionError(
            "Cannot read the golden sysout: " + sysoutFile.getAbsolutePath(), e);
      }
    }
    return Optional.empty();
  }

  /**
   * Runs JFlex on the spec of the test.
   *
   * @param sysout where JFlex prints to, {@code System.out} if empty
   */
  private static String invokeJflex(TestSpec spec, Optional<DiffOutputStream> sysout) {
    Options options = new Options();
    options.setJLex(spec.jlexCompat());
    options.setDump(spec.dump());
    // verbose is default -- verbose_provided simulates the behaviour of an explicit -v on the
    // command line
    if (spec.verbose_provided()) {
      options.setVerbose(true);
      options.setProgress(true);
      options.setUnusedWarning(true);
    }
    // if both verbose_provided() and quiet() are present, we want quiet() to win
    if (spec.quiet()) {
      options.setVerbose(false);
      options.setProgress(false);
      options.setUnusedWarning(false);
    }
    options.setUnusedWarning(spec.warnUnused());
    File lexFile = new File(spec.lex());
    Set<File> existingJavaFiles = javaFilesIn(lexFile.getAbsoluteFile().getParentFile());
    Out out = sysout.isPresent() ? new Out(options, sysout.get()) : new Out(options);
    LexGenerator lexGenerator = new LexGenerator(lexFile, options, out);
    String lexerJavaFileName = Objects.requireNonNull(lexGenerator.generate());
    checkNoCollision(existingJavaFiles, new File(lexerJavaFileName));
    if (spec.minimizedDfaStatesCount() > 0) {
      assertEquals(
          spec.minimizedDfaStatesCount(),
          lexGenerator.minimizedDfaStatesCount(),
          "Number of minimized states in the DFA");
    }
    return lexerJavaFileName;
  }

  /** Returns the absolute paths of the {@code .java} files in {@code dir}. */
  private static Set<File> javaFilesIn(File dir) {
    Set<File> result = new HashSet<>();
    File[] files = dir.listFiles((d, name) -> name.endsWith(".java"));
    if (files != null) {
      for (File file : files) {
        result.add(file.getAbsoluteFile());
      }
    }
    return result;
  }

  /** Fails if the generated scanner already existed before the JFlex run. */
  private static void checkNoCollision(Set<File> existingJavaFiles, File generated) {
    if (existingJavaFiles.contains(generated.getAbsoluteFile())) {
      throw new AssertionError(
          "\""
              + generated
              + "\" already existed before this test generated it. Make sure tests"
              + " have separate output files and the working directory is clean.");
    }
  }
}
