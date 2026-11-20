/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.testsuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.QuietOutputExtension;
import de.jflex.testing.diff.DiffOutputStream;
import de.jflex.testing.testsuite.annotations.NoExceptionThrown;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import de.jflex.util.javac.JavacUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import jflex.core.unicode.CharClasses;
import jflex.dfa.DFA;
import jflex.generator.LexGenerator;
import jflex.l10n.ErrorMessages;
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
    Class<?> testClass;

    /** Whether the spec expects JFlex to generate a scanner. */
    boolean expectsLexer;

    Throwable generateFailure;
    Throwable compileFailure;

    /** What JFlex printed. */
    final ByteArrayOutputStream jflexOutput = new ByteArrayOutputStream();

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
    build.testClass = testClass;
    build.expectsLexer = spec.generatorThrows() == NoExceptionThrown.class;
    String lexerJavaFileName = null;
    try {
      lexerJavaFileName = generateLexer(spec, build.jflexOutput);
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
    tests.add(
        DynamicTest.dynamicTest("Generate Lexer", () -> rethrow(build.generateFailure, build)));
    if (build.expectsLexer) {
      tests.add(
          DynamicTest.dynamicTest(
              "Compile java code",
              () -> {
                Assumptions.assumeTrue(
                    build.generateFailure == null, "Generating the lexer failed");
                rethrow(build.compileFailure, build);
              }));
    }
    return tests;
  }

  /** Throws the failure, if any, after printing what JFlex printed for the failing test. */
  private static void rethrow(Throwable failure, Build build) throws Throwable {
    if (failure != null) {
      if (build.jflexOutput.size() > 0) {
        QuietOutputExtension.printOutputOfFailedTest(
            build.testClass.getName() + ", JFlex",
            build.jflexOutput.toString(Charset.defaultCharset().name()));
      }
      throw failure;
    }
  }

  /**
   * Runs JFlex on the spec and checks its output and exceptions against the spec.
   *
   * @param jflexOutput where JFlex prints to
   * @return the generated file, or null if the spec expects JFlex to fail
   */
  private static String generateLexer(TestSpec spec, ByteArrayOutputStream jflexOutput)
      throws IOException {
    int outputOptions =
        (spec.sysout().isEmpty() ? 0 : 1)
            + (spec.output().length == 0 ? 0 : 1)
            + (spec.noOutput() ? 1 : 0);
    if (outputOptions > 1) {
      throw new IllegalStateException(
          "@TestSpec: at most one of sysout, output and noOutput can be set");
    }
    String lexerJavaFileName = null;
    Options options = new Options();
    Out out = new Out(options, jflexOutput);
    if (spec.generatorThrows() == NoExceptionThrown.class) {
      lexerJavaFileName = invokeJflex(spec, options, out);
    } else {
      generateLexerWithExpectedException(spec, options, out);
    }
    checkWarnings(spec, out.reportedWarnings());
    checkOutput(spec, jflexOutput);
    return lexerJavaFileName;
  }

  /** Compares the warnings of the test run with the {@code warnings} of the spec. */
  private static void checkWarnings(TestSpec spec, List<Out.Warning> reported) {
    List<String> reportedWarnings = new ArrayList<>();
    for (Out.Warning warning : reported) {
      reportedWarnings.add(
          describe(warning.message, fileName(warning), warning.line, warning.column));
    }
    List<String> expectedWarnings = new ArrayList<>();
    Warn[] expected = spec.warnings();
    for (int i = 0; i < expected.length; i++) {
      Warn warn = expected[i];
      String file = warn.file();
      int line = warn.line();
      int column = warn.column();
      if (i < reported.size()) {
        Out.Warning warning = reported.get(i);
        if (file.isEmpty()) file = fileName(warning);
        if (line == 0) line = warning.line;
        if (column == 0) column = warning.column;
      }
      expectedWarnings.add(describe(warn.value(), file, line, column));
    }
    assertEquals(
        String.join("\n", expectedWarnings),
        String.join("\n", reportedWarnings),
        "@TestSpec(warnings): warnings differ");
  }

  /** The name of the file of the warning without directories, empty if it has no file. */
  private static String fileName(Out.Warning warning) {
    return warning.file == null ? "" : warning.file.getName();
  }

  /** A warning as text, e.g. {@code NEVER_MATCH (a.flex, line 3)}. */
  private static String describe(ErrorMessages message, String file, int line, int column) {
    List<String> position = new ArrayList<>();
    if (!file.isEmpty()) position.add(file);
    if (line > 0) position.add("line " + line);
    if (column > 0) position.add("column " + column);
    return position.isEmpty()
        ? message.name()
        : message.name() + " (" + String.join(", ", position) + ")";
  }

  private static void generateLexerWithExpectedException(TestSpec spec, Options options, Out out) {
    try {
      invokeJflex(spec, options, out);
    } catch (AssertionError e) {
      // Not an exception of the generator, e.g. a failed check of the DFA: report the failure
      // itself.
      throw e;
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
   * Compares what JFlex printed with the golden {@code sysout} file, the {@code output} or the
   * {@code noOutput} of the spec, if one of them is set.
   *
   * <p>Compares after the generator has run and not while it prints, so that the complete output of
   * JFlex is available when the comparison fails.
   */
  private static void checkOutput(TestSpec spec, ByteArrayOutputStream jflexOutput)
      throws IOException {
    Reader expected;
    String message;
    if (!spec.sysout().isEmpty()) {
      File sysoutFile = new File(spec.sysout());
      try {
        expected = Files.newBufferedReader(sysoutFile.toPath(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new AssertionError(
            "Cannot read the golden sysout: " + sysoutFile.getAbsolutePath(), e);
      }
      message = "System.out differs: " + spec.sysout();
    } else if (spec.output().length > 0) {
      // JFlex ends its lines with the line separator of the platform.
      expected = new StringReader(String.join(Out.NL, spec.output()) + Out.NL);
      message = "@TestSpec(output): the output of JFlex differs";
    } else if (spec.noOutput()) {
      expected = new StringReader("");
      message = "@TestSpec(noOutput): JFlex must not print anything";
    } else {
      return;
    }
    DiffOutputStream diff = new DiffOutputStream(expected);
    diff.setComparisonFailureHandler(
        failure -> {
          throw new AssertionError(message, failure);
        });
    try {
      jflexOutput.writeTo(diff);
      diff.close();
    } finally {
      // Either the content was checked above, or a failure is already on its way.
      diff.closeWithoutCheck();
    }
  }

  /**
   * Runs JFlex on the spec of the test.
   *
   * @param options the default options, to set from the spec
   * @param out where JFlex prints to, reads {@code options}
   */
  private static String invokeJflex(TestSpec spec, Options options, Out out) {
    options.setJLex(spec.jlexCompat());
    options.setDump(spec.dump());
    if (!spec.skeleton().isEmpty()) {
      options.setSkeleton(new File(spec.skeleton()));
    }
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
    LexGenerator lexGenerator = new LexGenerator(lexFile, options, out);
    String lexerJavaFileName = Objects.requireNonNull(lexGenerator.generate());
    checkNoCollision(existingJavaFiles, new File(lexerJavaFileName));
    if (spec.minimizedDfaStatesCount() > 0) {
      assertEquals(
          spec.minimizedDfaStatesCount(),
          lexGenerator.minimizedDfaStatesCount(),
          "Number of minimized states in the DFA");
    }
    checkMinimizedDfa(spec, lexGenerator);
    checkCharClasses(spec, lexGenerator.charClasses());
    return lexerJavaFileName;
  }

  /**
   * Checks the minimized DFA against the canonical minimized DFA spec. Also assert that there are
   * no unreachable and no dead states.
   */
  private static void checkMinimizedDfa(TestSpec spec, LexGenerator lexGenerator) {
    DFA dfa = lexGenerator.minimizedDfa();
    assertEquals(
        lexGenerator.minimizedDfaStatesCount(),
        dfa.numStates(),
        "Number of states of the minimized DFA that can be reached from an entry state");
    assertFalse(
        dfa.hasDeadStates(),
        "The minimized DFA has a transition to a state from which no final state can be reached");
    if (spec.minimizedDfa().length > 0) {
      if (spec.charClasses().length == 0
          && spec.sameCharClass().length == 0
          && spec.distinctCharClasses().length == 0) {
        throw new IllegalStateException(
            "@TestSpec(minimizedDfa) needs charClasses, or sameCharClass and distinctCharClasses:"
                + " the inputs of the DFA are the numbers of the character classes");
      }
      // as one string each, so that a failure shows the DFAs line by line
      assertEquals(
          String.join("\n", spec.minimizedDfa()),
          String.join("\n", dfa.canonicalLines()),
          "Minimized DFA");
    }
  }

  /**
   * Checks the character classes against the spec. That the classes are a partition of all
   * characters is checked for every spec.
   */
  private static void checkCharClasses(TestSpec spec, CharClasses charClasses) {
    assertTrue(
        charClasses.invariants(),
        "Character classes must be disjoint and cover all characters:\n" + charClasses);
    if (spec.charClassesCount() > 0) {
      assertEquals(
          spec.charClassesCount(),
          charClasses.getNumClasses(),
          "Number of character classes in:\n" + charClasses);
    }
    if (spec.charClasses().length > 0) {
      List<String> actual = new ArrayList<>();
      for (int i = 0; i < charClasses.getNumClasses(); i++) {
        actual.add(charClasses.toString(i));
      }
      assertEquals(Arrays.asList(spec.charClasses()), actual, "Character classes");
    }
    for (String group : spec.sameCharClass()) {
      int[] codePoints = group.codePoints().toArray();
      for (int codePoint : codePoints) {
        assertEquals(
            charClasses.getClassCode(codePoints[0]),
            charClasses.getClassCode(codePoint),
            String.format(
                "sameCharClass \"%s\": U+%04X and U+%04X must be in the same character class"
                    + " of:%n%s",
                group, codePoints[0], codePoint, charClasses));
      }
    }
    for (String group : spec.distinctCharClasses()) {
      int[] codePoints = group.codePoints().toArray();
      for (int i = 0; i < codePoints.length; i++) {
        for (int j = i + 1; j < codePoints.length; j++) {
          assertNotEquals(
              charClasses.getClassCode(codePoints[i]),
              charClasses.getClassCode(codePoints[j]),
              String.format(
                  "distinctCharClasses \"%s\": U+%04X and U+%04X must be in different character"
                      + " classes of:%n%s",
                  group, codePoints[i], codePoints[j], charClasses));
        }
      }
    }
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
