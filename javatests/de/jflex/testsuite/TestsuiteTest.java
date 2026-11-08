/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testsuite;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import jflex.Main;
import jflex.core.OptionUtils;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.SilentExit;
import jflex.logging.Out;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Runs the {@code .test} cases in {@code testsuite/testcases/src/test/cases}.
 *
 * <p>Each case runs JFlex on {@code <name>.flex} and compares its messages with {@code
 * <name>-flex.output}. Unless JFlex is expected to fail, it then compiles the scanner, runs it on
 * the case's input files and compares what it prints with the {@code .output} goldens. The format
 * of {@code .test} files is defined by {@link TestLoader}.
 *
 * <p>System properties:
 *
 * <ul>
 *   <li>{@code jflex.testsuite.dir}: the {@code testsuite/testcases} directory. Paths in JFlex
 *       messages are relative to it.
 *   <li>{@code jflex.testsuite.out}: where generated scanners go.
 *   <li>{@code jflex.testsuite.cases}: optional comma-separated list of case directories to run,
 *       e.g. {@code dot,unicode-scripts}. All by default.
 * </ul>
 */
@RunWith(Parameterized.class)
public class TestsuiteTest {

  private static final File SUITE_DIR =
      canonical(new File(System.getProperty("jflex.testsuite.dir", "../testsuite/testcases")));

  private static final File CASES_DIR = new File(SUITE_DIR, "src/test/cases");

  private static final File OUT_DIR =
      new File(System.getProperty("jflex.testsuite.out", "target/testsuite"));

  private static Locale savedLocale;

  @Parameter(0)
  public String caseName;

  @Parameter(1)
  public File testFile;

  @Parameters(name = "{0}")
  public static Collection<Object[]> cases() {
    Set<String> selected = new HashSet<>();
    for (String dir : System.getProperty("jflex.testsuite.cases", "").split(",")) {
      if (!dir.trim().isEmpty()) {
        selected.add(dir.trim());
      }
    }
    Set<String> missing = new HashSet<>(selected);
    List<Object[]> result = new ArrayList<>();
    for (File dir : sorted(CASES_DIR.listFiles(File::isDirectory))) {
      if (!selected.isEmpty() && !selected.contains(dir.getName())) {
        continue;
      }
      missing.remove(dir.getName());
      for (File test : sorted(dir.listFiles((d, name) -> name.endsWith(".test")))) {
        result.add(new Object[] {dir.getName() + "/" + test.getName(), test});
      }
    }
    if (!missing.isEmpty()) {
      throw new IllegalArgumentException(
          "No test case directories " + missing + " in " + CASES_DIR);
    }
    if (result.isEmpty()) {
      throw new IllegalStateException("No test cases in " + CASES_DIR);
    }
    return result;
  }

  @BeforeClass
  public static void setLocale() {
    // JFlex messages are localised; the goldens are in English.
    savedLocale = Locale.getDefault();
    Locale.setDefault(Locale.US);
  }

  @AfterClass
  public static void restoreLocale() {
    Locale.setDefault(savedLocale);
  }

  @Test
  public void run() throws Exception {
    SuiteCase spec = load(testFile);
    File dir = testFile.getParentFile();
    String fileName = testFile.getName();
    assertEquals("name: in " + testFile, fileName.substring(0, fileName.length() - 5), spec.name);

    File outDir = new File(OUT_DIR, caseName.substring(0, caseName.length() - 5));
    recreate(outDir);

    boolean generated = runJFlex(spec, new File(dir, spec.name + ".flex"), outDir);
    if (spec.expectJFlexFail) {
      return;
    }
    assertTrue("JFlex failed on " + spec.name + ".flex", generated);

    compile(outDir);
    String className =
        spec.name.substring(0, 1).toUpperCase(Locale.ENGLISH) + spec.name.substring(1);

    if (spec.commonInputFile != null) {
      runScanner(
          spec,
          outDir,
          className,
          new File(dir, spec.commonInputFile),
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
    OptionUtils.setDefaultOptions();
    Out.setOutputStream(messages);
    try {
      Main.generate(args.toArray(new String[0]));
      success = true;
    } catch (GeneratorException | SilentExit e) {
      success = false;
    } finally {
      Out.setOutputStream(System.out);
      OptionUtils.setDefaultOptions();
    }
    if (success == spec.expectJFlexFail) {
      fail(
          (success ? "JFlex succeeded on " : "JFlex failed on ")
              + flexFile
              + ", expected otherwise. Its output:\n"
              + messages.toString(Charset.defaultCharset().name()));
    }

    // Paths in the goldens are relative to SUITE_DIR, with either kind of separator.
    String actual =
        messages
            .toString(Charset.defaultCharset().name())
            .replace(SUITE_DIR.getPath() + File.separator, "")
            .replace('\\', '/');
    File golden = new File(flexFile.getParentFile(), spec.name + "-flex.output");
    String expected = new String(Files.readAllBytes(golden.toPath()), UTF_8).replace('\\', '/');
    assertEquals("JFlex output, golden " + golden, lines(expected), lines(actual));
    return success;
  }

  private static void compile(File outDir) throws Exception {
    File[] sources = outDir.listFiles((d, name) -> name.endsWith(".java"));
    assertTrue("JFlex generated no Java file in " + outDir, sources.length > 0);
    JavacUtils.compile(Arrays.asList(sources), outDir);
  }

  /**
   * Runs the scanner's {@code main} on {@code input} and compares what it prints to {@code
   * expected}.
   */
  private static void runScanner(
      SuiteCase spec, File outDir, String className, File input, File expected) throws Exception {
    assertTrue("Missing input " + input, input.isFile());
    assertTrue("Missing golden " + expected, expected.isFile());

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream savedOut = System.out;
    // Parent null: only the JDK and the scanner, as when running it on its own.
    try (URLClassLoader loader = new URLClassLoader(new URL[] {outDir.toURI().toURL()}, null)) {
      Method main = loader.loadClass(className).getMethod("main", String[].class);
      System.setOut(new PrintStream(output, true, spec.outputFileEncoding));
      String[] args = {"--encoding", spec.inputFileEncoding, input.getPath()};
      main.invoke(null, (Object) args);
    } catch (InvocationTargetException e) {
      throw new AssertionError("Scanner " + className + " threw on " + input, e.getCause());
    } finally {
      System.setOut(savedOut);
    }

    String expectedText =
        new String(Files.readAllBytes(expected.toPath()), spec.outputFileEncoding);
    assertEquals(
        "Scanner output, golden " + expected,
        lines(expectedText),
        lines(output.toString(spec.outputFileEncoding)));
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
        assertTrue("Cannot delete " + f, f.isFile() && f.delete());
      }
    } else {
      assertTrue("Cannot create " + dir, dir.mkdirs());
    }
    assertFalse("Not empty: " + dir, dir.list().length > 0);
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
