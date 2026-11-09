/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.testsuite;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import de.jflex.testing.diff.DiffOutputStream;
import de.jflex.testing.testsuite.annotations.NoExceptionThrown;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import jflex.core.OptionUtils;
import jflex.generator.LexGenerator;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class JFlexTestRunner extends BlockJUnit4ClassRunner {

  private final Class<?> klass;
  private final TestSpec spec;
  private final PrintStream originalSysOut = System.out;
  private final PrintStream originalSysErr = System.err;

  public JFlexTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
    this.klass = testClass;
    this.spec =
        checkNotNull(
            testClass.getAnnotation(TestSpec.class),
            "A test running with %s must have a @%s",
            JFlexTestRunner.class.getName(),
            TestSpec.class.getName());
  }

  /**
   * Describes everything that {@link #run} reports. The JUnit Platform drops tests and failures
   * that are not part of this description.
   */
  @Override
  public Description getDescription() {
    Description description = super.getDescription();
    description.addChild(generateLexerDescription());
    if (expectsLexer()) {
      description.addChild(compileLexerDescription());
    }
    return description;
  }

  private Description generateLexerDescription() {
    return Description.createTestDescription(klass, "Generate Lexer");
  }

  private Description compileLexerDescription() {
    return Description.createTestDescription(klass, "Compile java code");
  }

  private boolean expectsLexer() {
    return spec.generatorThrows() == NoExceptionThrown.class;
  }

  @Override
  public void run(RunNotifier notifier) {
    String lexerJavaFileName = generateLexer(notifier);

    if (lexerJavaFileName != null) {
      buildLexer(notifier, lexerJavaFileName);
    } else if (expectsLexer()) {
      // Generating the lexer failed, there is nothing to compile.
      notifier.fireTestIgnored(compileLexerDescription());
    }

    // The lexer must be generated before the other tests are executed, as they can try to
    // compile this generated code.
    super.run(notifier);
  }

  private String generateLexer(RunNotifier notifier) {
    Description desc = generateLexerDescription();
    notifier.fireTestStarted(desc);

    Optional<DiffOutputStream> diffSysOut = injectDiffSysOut();
    Optional<DiffOutputStream> diffSysErr = injectDiffSysErr();

    try {
      String lexerJavaFileName;
      if (expectsLexer()) {
        lexerJavaFileName = invokeJflex();
      } else {
        lexerJavaFileName = null;
        generateLexerWithExpectedException();
      }

      assertSystemStream(diffSysOut, "System.out");
      assertSystemStream(diffSysErr, "System.err");

      return lexerJavaFileName;
    } catch (Throwable th) {
      notifier.fireTestFailure(new Failure(desc, th));
      return null;
    } finally {
      if (diffSysOut.isPresent()) {
        Out.setOutputStream(originalSysOut);
      }
      if (diffSysErr.isPresent()) {
        System.setErr(originalSysErr);
      }
      // The content was checked above in assertSystemStream so close without further checks.
      diffSysOut.ifPresent(DiffOutputStream::closeWithoutCheck);
      diffSysErr.ifPresent(DiffOutputStream::closeWithoutCheck);
      notifier.fireTestFinished(desc);
    }
  }

  private static void assertSystemStream(Optional<DiffOutputStream> diffStream, String streamName) {
    diffStream.ifPresent(
        s ->
            assertWithMessage("Content printed to %s", streamName)
                .that(s.remainingContent())
                .isEmpty());
  }

  private String generateLexerWithExpectedException() {
    try {
      invokeJflex();
    } catch (Throwable e) {
      assertWithMessage(
              "@TestCase indicates that the jflex generation must throw a "
                  + spec.generatorThrows().getSimpleName())
          .that(e)
          .isInstanceOf(spec.generatorThrows());
      if (spec.generatorThrowableCause() == Void.class) {
        assertWithMessage("@TestCase indicates that there is no cause for the generator exception")
            .that(e.getCause())
            .isNull();
      } else if (spec.generatorThrowableCause() != NoExceptionThrown.class) {
        assertWithMessage(
                "@TestCase indicates that cause of the generator exception is %s but it was %s\n",
                spec.generatorThrowableCause().getSimpleName(), e.getCause())
            .that(e.getCause())
            .isInstanceOf(spec.generatorThrowableCause());
      }
      // Expected
      return null;
    }
    throw new AssertionError(
        "@TestCase indicates that the jflex generation throws a "
            + spec.generatorThrows().getSimpleName()
            + " but nothing was thrown");
  }

  private Optional<DiffOutputStream> injectDiffSysOut() {
    if (!Strings.isNullOrEmpty(spec.sysout())) {
      File sysoutFile = new File(spec.sysout());
      try {
        DiffOutputStream diffSysOut =
            new DiffOutputStream(Files.newReader(sysoutFile, StandardCharsets.UTF_8));
        diffSysOut.setComparisonFailureHandler(
            failure -> {
              throw new Error("System.out differs: " + spec.sysout(), failure);
            });
        Out.setOutputStream(diffSysOut);
        return Optional.of(diffSysOut);
      } catch (FileNotFoundException e) {
        throw new AssertionError(
            "The golden sysout was not found: " + sysoutFile.getAbsolutePath(), e);
      }
    }
    return Optional.empty();
  }

  private Optional<DiffOutputStream> injectDiffSysErr() {
    if (!Strings.isNullOrEmpty(spec.syserr())) {
      File syserrFile = new File(spec.syserr());
      try {
        DiffOutputStream diffSysErr =
            new DiffOutputStream(Files.newReader(syserrFile, StandardCharsets.UTF_8));
        diffSysErr.setComparisonFailureHandler(
            failure -> {
              throw new Error("System.err differs: " + spec.syserr(), failure);
            });
        System.setErr(new PrintStream(diffSysErr));
        return Optional.of(diffSysErr);
      } catch (FileNotFoundException e) {
        throw new AssertionError(
            "The golden syserr was not found: " + syserrFile.getAbsolutePath(), e);
      }
    }
    return Optional.empty();
  }

  @SuppressWarnings("OrphanedFormatString")
  private String invokeJflex() {
    OptionUtils.setDefaultOptions();
    Options.jlex = spec.jlexCompat();
    Options.dump = spec.dump();
    // verbose is default -- verbose_provided simulates the behaviour of an explicit -v on the
    // command line
    if (spec.verbose_provided()) {
      Options.verbose = true;
      Options.progress = true;
      OptionUtils.set_unused_warning(true);
    }
    // if both verbose_provided() and quiet() are present, we want quiet() to win
    if (spec.quiet()) {
      Options.verbose = false;
      Options.progress = false;
      OptionUtils.set_unused_warning(false);
    }
    OptionUtils.set_unused_warning(spec.warnUnused());
    File lexFile = new File(spec.lex());
    Set<File> existingJavaFiles = javaFilesIn(lexFile.getAbsoluteFile().getParentFile());
    LexGenerator lexGenerator = new LexGenerator(lexFile);
    String lexerJavaFileName = checkNotNull(lexGenerator.generate());
    checkNoCollision(existingJavaFiles, new File(lexerJavaFileName));
    if (spec.minimizedDfaStatesCount() > 0) {
      assertWithMessage("There should be %d minimized states in the DFA")
          .that(lexGenerator.minimizedDfaStatesCount())
          .isEqualTo(spec.minimizedDfaStatesCount());
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

  private void buildLexer(RunNotifier notifier, String lexerJavaFileName) {
    Description desc = compileLexerDescription();
    notifier.fireTestStarted(desc);
    try {
      JavacUtils.compile(ImmutableList.of(new File(lexerJavaFileName)));
    } catch (Throwable th) {
      notifier.fireTestFailure(new Failure(desc, th));
    } finally {
      notifier.fireTestFinished(desc);
    }
  }
}
