/*
 * Copyright (C) 2019-2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.migration.testcase;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.logging.Level;
import org.apache.velocity.runtime.parser.ParseException;

/**
 * Tool to migrate a test case from {@code //testsuite/testcases/src/test/cases} (as executed by the
 * jflex-testsuite-amven-plugin) to {@code //ajvatests/jflex/testcase} (as executed by bazel).
 *
 * <p>See <a href="README.md">README</a> for usage.
 */
public class Migrator {

  private static final String PATH = JavaPackageUtils.getPathForClass(Migrator.class);
  private static final String TEST_CASE_TEMPLATE = PATH + "/TestCase.java.vm";
  private static final String BUILD_HEADER = "java/" + PATH + "/BUILD-header.bzl";
  private static final String BUILD_TEMPLATE = PATH + "/BUILD.vm";
  private static final String GOLDEN_INPUT_EXT = ".input";
  private static final String GOLDEN_OUTPUT_EXT = ".output";

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  public static final String TESTING_PACKAGE = "de.jflex.testcase.";
  private static final String TESTING_PACKAGE_DIR =
      TESTING_PACKAGE.replace('.', File.separatorChar);

  public static void main(String[] args) {
    LoggerConfig.of(logger).setLevel(Level.FINEST);
    checkArgument(args.length > 0, "Syntax error: migrator TESTCASE_DIRS_ABS_PATH");
    try {
      for (String testCaseDir : args) {
        migrateCase(testCaseDir);
      }
    } catch (MigrationException e) {
      logger.atSevere().withCause(e).log("Migration failed");
    }
  }

  /** Migrates one given test-case directory. */
  private static void migrateCase(String testCase) throws MigrationException {
    File dir = new File(testCase);
    if (!dir.exists()) {
      logger.atWarning().log("Directory doesn't exist: " + dir.getName());
      throw new MigrationException(
          "Could not migrate " + testCase, new FileNotFoundException(dir.getAbsolutePath()));
    }
    migrateCase(dir);
  }

  /**
   * Migrates one given test-case directory.
   *
   * <ol>
   *   <li>Creates a target folder in {@code /tmp} based on the original directory name (replaces
   *       '-' by '_')
   *   <li>Initializes a BUILD file
   *   <li>Finds all test specs, and migrate them.
   * </ol>
   */
  private static void migrateCase(File testCaseDir) throws MigrationException {
    logger.atInfo().log("Migrating %s...", testCaseDir.getName());
    logger.atFine().log("location: %s", testCaseDir.getAbsolutePath());
    File outputDir = initTargetDir(testCaseDir);
    File buildFile = initBuildFile(outputDir);
    Iterable<File> originalDirectoryContent = Files.fileTraverser().breadthFirst(testCaseDir);
    ImmutableList<File> testSpecFiles =
        Streams.stream(originalDirectoryContent)
            .filter(f -> Files.getFileExtension(f.getName()).equals("test"))
            .sorted(Comparator.comparing(File::getName))
            .collect(toImmutableList());
    for (File testSpec : testSpecFiles) {
      migrateTestCase(testCaseDir, testSpec, buildFile);
    }
  }

  private static File initTargetDir(File testCaseDir) {
    String lowerUnderscoreTestDir =
        CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, testCaseDir.getName());
    File outputDir = new File(new File("/tmp"), lowerUnderscoreTestDir);
    if (!outputDir.isDirectory()) {
      //noinspection ResultOfMethodCallIgnored
      outputDir.mkdirs();
    }
    return outputDir;
  }

  /** Creates a new BUILD file in the given directory. If there is one, it is replaced. */
  private static File initBuildFile(File outputDir) throws MigrationException {
    File outFile = new File(outputDir, "BUILD.bazel");
    outFile.delete();
    try {
      Files.copy(new File(BUILD_HEADER), outFile);
      return outFile;
    } catch (IOException e) {
      throw new MigrationException("Could not create BUILD file", e);
    }
  }

  /**
   * Migrates one given test case (such as {@code test-0}) within the test case directory.
   *
   * <p>Scans the grammar specification and creates a {@link TestCase} model.
   */
  private static void migrateTestCase(File testCaseDir, File testSpecFile, File buildFile)
      throws MigrationException {
    try (BufferedReader reader = Files.newReader(testSpecFile, StandardCharsets.UTF_8)) {
      TestSpecScanner scanner = new TestSpecScanner(reader);
      TestCase test = scanner.load();
      if (test.isExpectJavacFail() || test.isExpectJFlexFail()) {
        logger.atWarning().log("Test %s must be migrated with JflexTestRunner", test.getTestName());
      }
      migrateTestCase(testCaseDir, test, buildFile);
    } catch (IOException e) {
      throw new MigrationException("Failed reading the test spec " + testSpecFile.getName(), e);
    }
  }

  /**
   * Migrates one given test case (such as {@code test-0}), represented by a {@link TestCase} data
   * model, within the test case directory.
   *
   * <p>Creates all velocity {@link MigrationTemplateVars} for this case.
   */
  private static void migrateTestCase(File testCaseDir, TestCase test, File buildFile)
      throws MigrationException {
    File flexFile = findFlexFile(testCaseDir, test);
    ImmutableList<GoldenInOutFilePair> goldenFiles = findGoldenFiles(testCaseDir, test);
    String lowerUnderscoreTestDir = buildFile.getParentFile().getName();
    MigrationTemplateVars templateVars =
        createTemplateVars(lowerUnderscoreTestDir, test, flexFile, goldenFiles);
    migrateTestCase(buildFile, templateVars);
  }

  /**
   * Migrates one given test case.
   *
   * <ol>
   *   <li>Add targets to BUILD file
   *   <li>Renders the java test class
   *   <li>Modifies and copies the flex grammar in the target folder. See {@link
   *       #copyGrammarFile(File, String, File)}
   *   <li>Copies the golden files
   * </ol>
   */
  private static void migrateTestCase(File buildFile, MigrationTemplateVars templateVars)
      throws MigrationException {
    File outputDir = buildFile.getParentFile();
    logger.atInfo().log("Generating into %s", outputDir);
    renderBuildFile(templateVars, buildFile);
    renderTestCase(templateVars, outputDir);
    copyGrammarFile(templateVars.flexGrammar, templateVars.javaPackage, outputDir);
    copyGoldenFiles(templateVars.goldens, outputDir);
    logger.atInfo().log("Import the files in your workspace");
    logger.atInfo().log(
        "   cp -r %s $(bazel info workspace)/javatests/de/jflex/testcase", outputDir);
  }

  /** Generates the BUILD file for this test case. */
  // FIXME This should be done once for the whole directory, but with many java_test()
  private static void renderBuildFile(MigrationTemplateVars templateVars, File buildFile)
      throws MigrationException {
    try (OutputStream outputStream = new FileOutputStream(buildFile, /*append=*/ true)) {
      logger.atInfo().log("Generating %s", buildFile);
      velocityRenderBuildFile(templateVars, outputStream);
    } catch (IOException e) {
      throw new MigrationException("Couldn't write into BUILD file", e);
    }
  }

  /** Generates the Java test class. */
  private static void renderTestCase(MigrationTemplateVars templateVars, File outputDir)
      throws MigrationException {
    File outFile = new File(outputDir, templateVars.testClassName + ".java");
    try (OutputStream outputStream = new FileOutputStream(outFile)) {
      logger.atInfo().log("Generating %s", outFile);
      velocityRenderTestCase(templateVars, outputStream);
    } catch (IOException e) {
      throw new MigrationException("Couldn't write java test case", e);
    }
  }

  /**
   * Copy the grammar file.
   *
   * <p>The old grammars were defined in the default (empty) java package. The copied file be
   * prepended with a {@code package} declaration matches its directory location.
   */
  private static void copyGrammarFile(File flexFile, String javaPackage, File outputDir)
      throws MigrationException {
    try {
      logger.atInfo().log("Copy grammar %s", flexFile.getName());
      logger.atFine().log("location: %s", flexFile.getAbsolutePath());
      // The grammars are defined in the default package. This is so bad practice that I'm not
      // sure that bazel allows compilation. Don't simply copy the original:
      // copyFile(fixedFlexFile, outputDir);
      // But instead:
      File copiedWithPatch = new File(outputDir, flexFile.getName());
      CharSink out = Files.asCharSink(copiedWithPatch, StandardCharsets.UTF_8);
      CharSource fixedContent =
          CharSource.concat(
              CharSource.wrap(String.format("package %s;\n", javaPackage)),
              Files.asCharSource(flexFile, StandardCharsets.UTF_8));

      fixedContent.copyTo(out);
    } catch (IOException e) {
      throw new MigrationException("Could not copy .flex file", e);
    }
  }

  /** Copy the list of golden files. */
  private static void copyGoldenFiles(ImmutableList<GoldenInOutFilePair> goldens, File outputDir)
      throws MigrationException {
    logger.atInfo().log("Copy %d pairs of golden files", goldens.size());
    try {
      for (GoldenInOutFilePair golden : goldens) {
        copyFile(golden.inputFile, outputDir);
        copyFile(golden.outputFile, outputDir);
      }
    } catch (IOException e) {
      throw new MigrationException("Could not copy golden files", e);
    }
  }

  /** Creates the template variables for velocity. */
  private static MigrationTemplateVars createTemplateVars(
      String lowerUnderscoreTestDir,
      TestCase test,
      File flexGrammar,
      ImmutableList<GoldenInOutFilePair> goldenFiles) {
    MigrationTemplateVars vars = new MigrationTemplateVars();
    vars.flexGrammar = flexGrammar;
    vars.javaPackage = TESTING_PACKAGE + lowerUnderscoreTestDir;
    vars.javaPackageDir = TESTING_PACKAGE_DIR + lowerUnderscoreTestDir;
    vars.testClassName =
        CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, test.getTestName()) + "GoldenTest";
    vars.testName = test.getTestName();
    vars.testDescription = Strings.nullToEmpty(test.getDescription()).trim();
    vars.goldens = goldenFiles;
    // TODO(regisd). We should use the real JFLex generator to read the `%class` value from the
    // grammar. For now, we rely on the convention that the name of the scanner is the name of
    // the test...
    vars.scannerClassName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, test.getTestName());
    return vars;
  }

  /** Lists all golden files for a given test. */
  private static ImmutableList<GoldenInOutFilePair> findGoldenFiles(
      File testCaseDir, TestCase test) {
    Iterable<File> dirContent = Files.fileTraverser().breadthFirst(testCaseDir);
    return Streams.stream(dirContent)
        .filter(f -> isGoldenInputFile(test, f))
        .sorted(Comparator.comparing(File::getName))
        .map(f -> new GoldenInOutFilePair(f, getGoldenOutputFile(f)))
        .filter(g -> g.outputFile.isFile())
        .collect(toImmutableList());
  }

  /** Finds the grammar file for a test. */
  private static File findFlexFile(File testCaseDir, TestCase test) {
    return new File(testCaseDir, test.getTestName() + ".flex");
  }

  private static boolean isGoldenInputFile(TestCase test, File f) {
    return f.getName().startsWith(test.getTestName() + "-") && f.getName().endsWith(".input");
  }

  /** Returns the output file for the given input file. */
  private static File getGoldenOutputFile(File goldenInputFIle) {
    checkArgument(goldenInputFIle.getName().endsWith(GOLDEN_INPUT_EXT));
    return new File(
        goldenInputFIle.getParentFile(),
        goldenInputFIle
                .getName()
                .substring(0, goldenInputFIle.getName().length() - ".input".length())
            + GOLDEN_OUTPUT_EXT);
  }

  /** Invokes velocity to generate the BUILD file. */
  private static void velocityRenderBuildFile(
      MigrationTemplateVars templateVars, OutputStream output)
      throws IOException, MigrationException {
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      Velocity.render(readResource(BUILD_TEMPLATE), "BuildBazel", templateVars, writer);
    } catch (ParseException e) {
      throw new MigrationException("Failed to parse Velocity template " + BUILD_TEMPLATE, e);
    }
  }

  /** Invokes velocity to generate the Test file. */
  private static void velocityRenderTestCase(
      MigrationTemplateVars templateVars, OutputStream output)
      throws IOException, MigrationException {
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      Velocity.render(readResource(TEST_CASE_TEMPLATE), "TestCase", templateVars, writer);
    } catch (ParseException e) {
      throw new MigrationException("Failed to parse Velocity template " + TEST_CASE_TEMPLATE, e);
    }
  }

  /** Copies file to the target directory. */
  private static void copyFile(File file, File targetDir) throws IOException {
    checkArgument(file.isFile(), "Input %s should be a file: %s", file, file.getAbsoluteFile());
    checkArgument(
        targetDir.isDirectory(),
        "Target %s should be a directory: %s",
        targetDir,
        targetDir.getAbsoluteFile());
    logger.atFine().log("Copying %s...", file.getName());
    File copiedFile = new File(targetDir, file.getName());
    Files.copy(file, copiedFile);
  }

  private static InputStreamReader readResource(String resourceName) {
    InputStream resourceAsStream =
        checkNotNull(
            ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName),
            "Null resource content for " + resourceName);
    return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
  }

  private Migrator() {}
}
