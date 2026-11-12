/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testsuite;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.jflex.testing.SystemOutCapture;
import de.jflex.util.javac.JavacUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import jflex.Main;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.SilentExit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Runs the {@code .test} cases in {@code src/test/cases} of a test module, and optionally the
 * Unicode cases that {@code de.jflex.testsuite.generator} generates from the Unicode Character
 * Database. A module runs them with an empty subclass named {@code TestsuiteTest}.
 *
 * <p>Each case runs JFlex on {@code <name>.flex} and compares its messages with {@code
 * <name>-flex.output}. Unless JFlex is expected to fail, it then compiles the scanner, runs it on
 * the case's input files and compares what it prints with the {@code .output} goldens. The format
 * of {@code .test} files is defined by {@link TestLoader}.
 *
 * <p>System properties:
 *
 * <ul>
 *   <li>{@code jflex.testsuite.dir}: the module directory, which has the cases in {@code
 *       src/test/cases}. Paths in JFlex messages are relative to it. The working directory by
 *       default.
 *   <li>{@code jflex.testsuite.generated.dir}: optional directory with the generated cases, in the
 *       same layout. Paths in JFlex messages are relative to it too. A {@code common-input-file}
 *       that the module directory does not have is taken from the same place in this directory.
 *   <li>{@code jflex.testsuite.out}: where generated scanners go.
 *   <li>{@code jflex.testsuite.cases}: optional comma-separated list of case directories to run,
 *       e.g. {@code dot,unicode-scripts}. All by default.
 * </ul>
 */
public abstract class AbstractTestsuiteTest {

  private static final File SUITE_DIR =
      canonical(new File(System.getProperty("jflex.testsuite.dir", ".")));

  private static final File CASES_DIR = new File(SUITE_DIR, "src/test/cases");

  /** The generated cases, or null for none. */
  private static final File GENERATED_SUITE_DIR =
      System.getProperty("jflex.testsuite.generated.dir", "").isEmpty()
          ? null
          : canonical(new File(System.getProperty("jflex.testsuite.generated.dir")));

  private static final File OUT_DIR =
      new File(System.getProperty("jflex.testsuite.out", "target/testsuite"));

  /** The name and the {@code .test} file of each case to run. */
  static List<Arguments> cases() {
    Set<String> selected = new HashSet<>();
    for (String dir : System.getProperty("jflex.testsuite.cases", "").split(",")) {
      if (!dir.trim().isEmpty()) {
        selected.add(dir.trim());
      }
    }
    Set<String> missing = new HashSet<>(selected);
    List<File> casesDirs = new ArrayList<>();
    casesDirs.add(CASES_DIR);
    if (GENERATED_SUITE_DIR != null) {
      File generated = new File(GENERATED_SUITE_DIR, "src/test/cases");
      if (!generated.isDirectory()) {
        throw new IllegalStateException("No generated test cases in " + generated);
      }
      casesDirs.add(generated);
    }
    List<Arguments> result = new ArrayList<>();
    for (File casesDir : casesDirs) {
      for (File dir : sorted(casesDir.listFiles(File::isDirectory))) {
        if (!selected.isEmpty() && !selected.contains(dir.getName())) {
          continue;
        }
        missing.remove(dir.getName());
        for (File test : sorted(dir.listFiles((d, name) -> name.endsWith(".test")))) {
          result.add(Arguments.of(dir.getName() + "/" + test.getName(), test));
        }
      }
    }
    if (!missing.isEmpty()) {
      throw new IllegalArgumentException(
          "No test case directories " + missing + " in " + casesDirs);
    }
    if (result.isEmpty()) {
      throw new IllegalStateException("No test cases in " + casesDirs);
    }
    return result;
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("cases")
  public void run(String caseName, File testFile) throws Exception {
    SuiteCase spec = load(testFile);
    File dir = testFile.getParentFile();
    String fileName = testFile.getName();
    assertEquals(fileName.substring(0, fileName.length() - 5), spec.name, "name: in " + testFile);

    File outDir = new File(OUT_DIR, caseName.substring(0, caseName.length() - 5));
    recreate(outDir);

    boolean generated = runJFlex(spec, new File(dir, spec.name + ".flex"), outDir);
    if (spec.expectJFlexFail) {
      return;
    }
    assertTrue(generated, "JFlex failed on " + spec.name + ".flex");

    compile(outDir);
    String className =
        spec.name.substring(0, 1).toUpperCase(Locale.ENGLISH) + spec.name.substring(1);

    if (spec.commonInputFile != null) {
      runScanner(
          spec,
          outDir,
          className,
          commonInputFile(dir, spec.commonInputFile),
          new File(dir, spec.name + ".output"));
    } else {
      // A case without input files only checks that the scanner compiles.
      String prefix = spec.name + "-";
      for (File input :
          sorted(dir.listFiles((d, name) -> name.startsWith(prefix) && name.endsWith(".input")))) {
        String inputName = input.getName();
        File expected = new File(dir, inputName.substring(0, inputName.length() - 6) + ".output");
        runScanner(spec, outDir, className, input, expected);
      }
    }
  }

  /**
   * The common input file of a case in {@code dir}. Inputs may be generated: if the suite directory
   * does not have the input, then take the file at the same sub path in the generated directory.
   */
  private static File commonInputFile(File dir, String path) {
    File input = new File(dir, path);
    if (!input.exists()
        && GENERATED_SUITE_DIR != null
        && dir.toPath().startsWith(SUITE_DIR.toPath())) {
      File generatedDir =
          GENERATED_SUITE_DIR
              .toPath()
              .resolve(SUITE_DIR.toPath().relativize(dir.toPath()))
              .toFile();
      return new File(generatedDir, path);
    }
    return input;
  }

  private static SuiteCase load(File testFile) throws IOException, LoadException {
    try (Reader reader = Files.newBufferedReader(testFile.toPath(), UTF_8)) {
      return new TestLoader(reader).load();
    }
  }

  /**
   * Runs JFlex on {@code flexFile}, writing to {@code outDir}, and checks its messages against
   * {@code <name>-flex.output}.
   *
   * @return whether JFlex succeeded
   */
  private static boolean runJFlex(SuiteCase spec, File flexFile, File outDir) throws IOException {
    List<String> args = new ArrayList<>(spec.jflexArgs);
    args.add("-d");
    args.add(outDir.getPath());
    args.add(flexFile.getPath());

    ByteArrayOutputStream messages = new ByteArrayOutputStream();
    boolean success;
    try {
      Main.generate(args.toArray(new String[0]), messages);
      success = true;
    } catch (GeneratorException | SilentExit e) {
      success = false;
    }
    if (success == spec.expectJFlexFail) {
      fail(
          (success ? "JFlex succeeded on " : "JFlex failed on ")
              + flexFile
              + ", expected otherwise. Its output:\n"
              + messages.toString(Charset.defaultCharset().name()));
    }

    // Paths in the goldens are relative to the suite directory, with either kind of separator.
    // The generated directory first: it is usually inside the suite directory.
    String actual = messages.toString(Charset.defaultCharset().name());
    if (GENERATED_SUITE_DIR != null) {
      actual = actual.replace(GENERATED_SUITE_DIR.getPath() + File.separator, "");
    }
    actual = actual.replace(SUITE_DIR.getPath() + File.separator, "");
    actual = actual.replace('\\', '/');
    File golden = new File(flexFile.getParentFile(), spec.name + "-flex.output");
    String expected = new String(Files.readAllBytes(golden.toPath()), UTF_8).replace('\\', '/');
    assertEquals(lines(expected), lines(actual), "JFlex output, golden " + golden);
    return success;
  }

  private static void compile(File outDir) throws Exception {
    File[] sources = outDir.listFiles((d, name) -> name.endsWith(".java"));
    assertTrue(sources.length > 0, "JFlex generated no Java file in " + outDir);
    JavacUtils.compile(Arrays.asList(sources), outDir);
  }

  /**
   * Runs the scanner's {@code main} on {@code input} and compares what it prints to {@code
   * expected}.
   */
  private static void runScanner(
      SuiteCase spec, File outDir, String className, File input, File expected) throws Exception {
    assertTrue(input.isFile(), "Missing input " + input);
    assertTrue(expected.isFile(), "Missing golden " + expected);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    // Parent null: only the JDK and the scanner, as when running it on its own.
    try (URLClassLoader loader = new URLClassLoader(new URL[] {outDir.toURI().toURL()}, null)) {
      Method main = loader.loadClass(className).getMethod("main", String[].class);
      String[] args = {"--encoding", spec.inputFileEncoding, input.getPath()};
      // Make sure to isolate the output of each concurrent thread
      try (SystemOutCapture capture =
          SystemOutCapture.start(new PrintStream(output, true, spec.outputFileEncoding))) {
        main.invoke(null, (Object) args);
      }
    } catch (InvocationTargetException e) {
      throw new AssertionError("Scanner " + className + " threw on " + input, e.getCause());
    }

    String expectedText =
        new String(Files.readAllBytes(expected.toPath()), spec.outputFileEncoding);
    assertEquals(
        lines(expectedText),
        lines(output.toString(spec.outputFileEncoding)),
        "Scanner output, golden " + expected);
  }

  /** The lines of {@code text}, joined with {@code \n}, so that line endings don't matter. */
  private static String lines(String text) throws IOException {
    StringBuilder result = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line).append('\n');
      }
    }
    return result.toString();
  }

  private static void recreate(File dir) throws IOException {
    if (dir.exists()) {
      for (File f : dir.listFiles()) {
        assertTrue(f.isFile() && f.delete(), "Cannot delete " + f);
      }
    } else {
      assertTrue(dir.mkdirs(), "Cannot create " + dir);
    }
    assertFalse(dir.list().length > 0, "Not empty: " + dir);
  }

  private static File[] sorted(File[] files) {
    if (files == null) {
      return new File[0];
    }
    Arrays.sort(files);
    return files;
  }

  private static File canonical(File f) {
    try {
      return f.getCanonicalFile();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
