/*
 * JFlex Maven3 plugin
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.maven.plugin.jflex;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.base.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoExtension;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.junit.jupiter.api.Test;

/**
 * @author Régis Décamps (decamps@users.sf.net)
 */
@MojoTest
public class JFlexMojoTest {

  /** The test projects. Their {@code plugin-config.xml} has the configuration of the mojo. */
  private static final String PROJECTS = "src/test/projects/";

  private static final String CONFIG = "/plugin-config.xml";

  /** The warning parameters of the mojo, with a part of the warning message to match. */
  private static final String[][] WARNINGS = {
    {"unusedWarning", "has been declared but never used"},
    {"neverMatchWarning", "Rule can never be matched"},
    {"emptyMatchWarning", "Expression matches the empty string"},
    {"ctorDebugWarning", "Emitting default constructor without parameters"},
    {"notAtBolWarning", "is not at the beginning of the line"},
    {"cupsymAfterCupWarning", "%cupsym should be used before %cup"},
    {"unicodeTooLongWarning", "Unicode escape sequence is too long"},
  };

  /** Executes the mojo without the generated files of a previous run. */
  private static void executeClean(JFlexMojo mojo) throws Exception {
    deleteRecursively(outputDirectory(mojo));
    mojo.execute();
  }

  /** Executes the mojo like {@link #executeClean} and returns what it printed. */
  private static String executeCleanAndCapture(JFlexMojo mojo) throws Exception {
    // The mojo prints JFlex messages to the System.out of the time it runs.
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream savedOut = System.out;
    System.setOut(new PrintStream(output, true));
    try {
      executeClean(mojo);
    } finally {
      System.setOut(savedOut);
    }
    return output.toString();
  }

  /** The output directory of the mojo. It is relative to the plugin directory. */
  private static File outputDirectory(JFlexMojo mojo) throws Exception {
    File outDir = MojoExtension.getVariableValueFromObject(mojo, "outputDirectory");
    return outDir.isAbsolute() ? outDir : new File(MojoExtension.getBasedir(), outDir.getPath());
  }

  /**
   * Deletes the generated files of a previous run. Otherwise the mojo considers them up to date and
   * does not run JFlex, which makes the tests depend on a clean {@code target/}.
   */
  private static void deleteRecursively(File dir) throws IOException {
    if (!dir.exists()) {
      return;
    }
    try (Stream<Path> paths = Files.walk(dir.toPath())) {
      for (Path path : paths.sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
        Files.delete(path);
      }
    }
  }

  /** Tests configuration with a single input file. */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "single-file-test" + CONFIG)
  public void testSingleFile(JFlexMojo mojo) throws Exception {
    executeClean(mojo);

    File produced = getExpectedOutputFile(mojo);
    assertWithMessage("produced file is a file: " + produced).that(produced.isFile()).isTrue();

    long size = produced.length();
    /*
     * NOTE: The final file size also depends on the employed line
     * terminator. For this reason, the generated output will be longer on a
     * Windows platform ("\r\n") than on a Unix platform ("\n").
     */
    boolean correctSize = (size > 26624) && (size < 36696);
    assertWithMessage("size of produced file between 26k and 36k. Actual is " + size)
        .that(correctSize)
        .isTrue();
  }

  /** Tests configuration with a single input directory. */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "single-dir-test" + CONFIG)
  public void testSingleDir(JFlexMojo mojo) throws Exception {
    executeClean(mojo);

    File produced = getExpectedOutputFile(mojo);
    assertWithMessage("produced file is a file: " + produced).that(produced.isFile()).isTrue();
  }

  /** Tests configuration with a single input directory containing sub directories. */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "recursion-test" + CONFIG)
  public void testRecursion(JFlexMojo mojo) throws Exception {
    executeClean(mojo);

    File produced = getExpectedOutputFile(mojo);
    assertWithMessage("produced file is a file: " + produced).that(produced.isFile()).isTrue();
  }

  /** A {@code %suppress all} in one spec does not suppress warnings in the next spec. */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "suppress-leak-test" + CONFIG)
  public void testSuppressDoesNotLeak(JFlexMojo mojo) throws Exception {
    String log = executeCleanAndCapture(mojo);
    // one warning, for warn.flex; none for suppress-all.flex (the Maven log mentions both files)
    String msg = "JFlex and Maven output:\n" + log;
    assertEquals(2, log.split("never be matched", -1).length, msg);
    assertTrue(log.contains("Warning in file \"") && log.contains("warn.flex\""), msg);
    assertFalse(log.contains("suppress-all.flex\""), msg);
  }

  /** All warnings are on by default. */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "warnings-test" + CONFIG)
  public void testWarningsOnByDefault(JFlexMojo mojo) throws Exception {
    String log = executeCleanAndCapture(mojo);

    for (String[] warning : WARNINGS) {
      assertTrue(log.contains(warning[1]), warning[0] + " on by default. Output:\n" + log);
    }
  }

  /** Each warning parameter only switches off its own warning. */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "warnings-test" + CONFIG)
  public void testSingleWarningOff(JFlexMojo mojo) throws Exception {
    for (String[] off : WARNINGS) {
      MojoExtension.setVariableValueToObject(mojo, off[0], false);
      String log = executeCleanAndCapture(mojo);
      MojoExtension.setVariableValueToObject(mojo, off[0], true);

      for (String[] warning : WARNINGS) {
        assertEquals(
            warning != off,
            log.contains(warning[1]),
            warning[0] + " with " + off[0] + " off. Output:\n" + log);
      }
    }
  }

  /**
   * {@code suppressAllWarnings} switches off all warnings, no matter what the single parameters
   * are.
   */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "warnings-test" + CONFIG)
  @MojoParameter(name = "suppressAllWarnings", value = "true")
  @MojoParameter(name = "neverMatchWarning", value = "true")
  public void testSuppressAllWarnings(JFlexMojo mojo) throws Exception {
    String log = executeCleanAndCapture(mojo);

    assertFalse(log.contains("Warning"), "Output:\n" + log);
    assertTrue(new File(outputDirectory(mojo), "Warnings.java").isFile());
  }

  /** Two mojos executing at the same time like in a parallel reactor build. */
  @Test
  public void testConcurrentExecutions(
      @InjectMojo(goal = "generate", pom = PROJECTS + "single-file-test" + CONFIG) JFlexMojo first,
      @InjectMojo(goal = "generate", pom = PROJECTS + "single-dir-test" + CONFIG) JFlexMojo second)
      throws Exception {
    deleteRecursively(outputDirectory(first));
    deleteRecursively(outputDirectory(second));
    CountDownLatch start = new CountDownLatch(2);
    ExecutorService executor = Executors.newFixedThreadPool(2);
    try {
      Future<?> firstDone = executor.submit(() -> execute(first, start));
      Future<?> secondDone = executor.submit(() -> execute(second, start));
      firstDone.get();
      secondDone.get();
    } finally {
      executor.shutdownNow();
    }

    File firstProduced = getExpectedOutputFile(first);
    File secondProduced = getExpectedOutputFile(second);
    assertWithMessage("produced file is a file: " + firstProduced)
        .that(firstProduced.isFile())
        .isTrue();
    assertWithMessage("produced file is a file: " + secondProduced)
        .that(secondProduced.isFile())
        .isTrue();
    assertFalse(firstProduced.equals(secondProduced));
  }

  /**
   * The encoding stays unset without a configured encoding and without a source encoding of the
   * project. {@link JFlexMojoEncodingTest} has tests with a source encoding.
   */
  @Test
  @InjectMojo(goal = "generate", pom = PROJECTS + "single-file-test" + CONFIG)
  public void testEncodingUnset(JFlexMojo mojo) throws Exception {
    String encoding = MojoExtension.getVariableValueFromObject(mojo, "encodingName");
    assertTrue(encoding == null || encoding.isEmpty(), "encoding: " + encoding);
  }

  /** Waits for the other mojo, then executes. */
  private static Void execute(JFlexMojo mojo, CountDownLatch start) throws Exception {
    start.countDown();
    start.await();
    mojo.execute();
    return null;
  }

  @Test
  public void extensionPredicate() {
    Predicate<File> predicate = new JFlexMojo.ExtensionPredicate("bar", "baz");
    assertThat(predicate.apply(new File("/tmp/foo.bar"))).isTrue();
    assertThat(predicate.apply(new File("/tmp/foo.baz"))).isTrue();
    assertThat(predicate.apply(new File("/tmp/foo.bar.too"))).isFalse();
    assertThat(predicate.apply(new File("/tmp/foo.blahblahbar"))).isFalse();
  }

  /**
   * Gets the expected path to the output file from jflex.
   *
   * @param mojo The jflex mojo under test.
   * @return The expected path to the output file from jflex.
   */
  private static File getExpectedOutputFile(JFlexMojo mojo) throws Exception {
    return new File(outputDirectory(mojo), "org/jamwiki/parser/JAMWikiPreProcessor.java");
  }
}
