package jflex.migration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.Files;
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
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.javac.JavaPackageUtil;
import jflex.velocity.Velocity;
import org.apache.velocity.runtime.parser.ParseException;

public class Migrator {

  private static final String TEST_CASE_TEMPLATE =
      JavaPackageUtil.getPathForClass(Migrator.class) + "/TestCase.java.vm";
  private static final String GOLDEN_INPUT_EXT = ".input";
  private static final String GOLDEN_OUTPUT_EXT = ".output";

  private static FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) {
    Preconditions.checkArgument(args.length > 0, "Syntax error: migrator TESTCASE_DIRS_ABS_PATH");
    try {
      for (String testCaseDir : args) {
        migrateCase(testCaseDir);
      }
    } catch (MigrationException e) {
      logger.atSevere().withCause(e).log("Migration failed");
    }
  }

  private static void migrateCase(String testCase) throws MigrationException {
    File dir = new File(testCase);
    if (!dir.exists()) {
      logger.atWarning().log("Directory doesn't exist: " + dir.getName());
      throw new MigrationException(
          "Could not migrate " + testCase, new FileNotFoundException(dir.getAbsolutePath()));
    }
    migrateCase(dir);
  }

  private static void migrateCase(File testCaseDir) throws MigrationException {
    logger.atInfo().log("Migrating %s...", testCaseDir.getName());
    Iterable<File> directoryContent = Files.fileTraverser().breadthFirst(testCaseDir);
    ImmutableList<File> testSpecFiles =
        Streams.stream(directoryContent)
            .filter(f -> Files.getFileExtension(f.getName()).equals("test"))
            .collect(toImmutableList());
    for (File testSpec : testSpecFiles) {
      migrateTestCase(testCaseDir, testSpec);
    }
  }

  private static void migrateTestCase(File testCaseDir, File testSpecFile)
      throws MigrationException {
    try (BufferedReader reader = Files.newReader(testSpecFile, Charsets.UTF_8)) {
      TestSpecScanner scanner = new TestSpecScanner(reader);
      TestCase test = scanner.load();
      if (test.isExpectJavacFail() || test.isExpectJFlexFail()) {
        logger.atWarning().log("Test %s must be migrated with JflexTestRunner", test.getTestName());
      }
      migrateTestCase(testCaseDir, test);
    } catch (IOException e) {
      throw new MigrationException("Failed reading the test spec " + testSpecFile.getName(), e);
    }
  }

  private static void migrateTestCase(File testCaseDir, TestCase test) throws MigrationException {
    String lowerUnderscoreTestDir =
        CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, testCaseDir.getName());
    ImmutableList<GoldenInOutFilePair> goldenFiles = findGoldenFiles(testCaseDir, test);
    TestCaseVars templateVars = createTemplateVars(lowerUnderscoreTestDir, test, goldenFiles);
    migrateTestCase(lowerUnderscoreTestDir, templateVars);
  }

  private static void migrateTestCase(String targetTestDir, TestCaseVars templateVars)
      throws MigrationException {
    File outputDir = new File(new File("/tmp"), targetTestDir);
    if (!outputDir.isDirectory()) {
      //noinspection ResultOfMethodCallIgnored
      outputDir.mkdirs();
    }
    renderTestCase(templateVars, outputDir);
    copyGoldenFiles(templateVars.goldens, outputDir);
  }

  private static void renderTestCase(TestCaseVars templateVars, File outputDir)
      throws MigrationException {
    File outFile = new File(outputDir, templateVars.testClassName + ".java");
    try (OutputStream outputStream = new FileOutputStream(outFile)) {
      logger.atInfo().log("Generating %s", outFile);
      render(templateVars, outputStream);
    } catch (IOException e) {
      throw new MigrationException("Couldn't write java test case", e);
    }
  }

  private static void copyGoldenFiles(ImmutableList<GoldenInOutFilePair> goldens, File outputDir)
      throws MigrationException {
    logger.atInfo().log("Copy Golden files to %s", outputDir.getAbsolutePath());
    try {
      for (GoldenInOutFilePair golden : goldens) {
        copyGoldenFile(golden.inputFile, outputDir);
        copyGoldenFile(golden.outputFile, outputDir);
      }
    } catch (IOException e) {
      throw new MigrationException("Could not copy golden files", e);
    }
  }

  private static TestCaseVars createTemplateVars(
      String lowerUnderscoreTestDir,
      TestCase test,
      ImmutableList<GoldenInOutFilePair> goldenFiles) {
    TestCaseVars vars = new TestCaseVars();
    vars.flexGrammar = new File(test.getTestName() + ".flex");
    vars.javaPackage = "jflex.testcase." + lowerUnderscoreTestDir;
    vars.javaPackageDir = "jflex/testcase/" + lowerUnderscoreTestDir;
    vars.testClassName =
        CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, test.getTestName()) + "GoldenTest";
    vars.testName = test.getTestName();
    vars.testDescription = test.getDescription().trim();
    vars.goldens = goldenFiles;
    // TODO(regisd). We should use the real JFLex generator to read the `%class` value from the
    // grammar. For now, we rely on the convention that the name of the scanner is the name of
    // the test...
    vars.scannerClassName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, test.getTestName());
    return vars;
  }

  private static ImmutableList<GoldenInOutFilePair> findGoldenFiles(
      File testCaseDir, TestCase test) {
    Iterable<File> dirContent = Files.fileTraverser().breadthFirst(testCaseDir);
    return Streams.stream(dirContent)
        .filter(f -> isGoldenInputFile(test, f))
        .map(f -> new GoldenInOutFilePair(test.getTestName(), f, getGoldenOutputFile(f)))
        .collect(toImmutableList());
  }

  private static boolean isGoldenInputFile(TestCase test, File f) {
    return f.getName().startsWith(test.getTestName() + "-") && f.getName().endsWith(".input");
  }

  private static File getGoldenOutputFile(File f) {
    Preconditions.checkArgument(f.getName().endsWith(GOLDEN_INPUT_EXT));
    return new File(
        f.getParentFile(),
        f.getName().substring(0, f.getName().length() - ".input".length()) + GOLDEN_OUTPUT_EXT);
  }

  private static void render(TestCaseVars templateVars, OutputStream output)
      throws IOException, MigrationException {
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
      Velocity.render(readResource(TEST_CASE_TEMPLATE), "TestCase", templateVars, writer);
    } catch (ParseException e) {
      throw new MigrationException("Failed to parse Velocity template " + TEST_CASE_TEMPLATE, e);
    }
  }

  private static void copyGoldenFile(File file, File targetTestDir) throws IOException {
    checkArgument(file.isFile());
    checkArgument(targetTestDir.isDirectory());
    logger.atFine().log("Copying %s...", file.getName());
    File cpInputFile = new File(targetTestDir, file.getName());
    Files.copy(file, cpInputFile);
  }

  private static InputStreamReader readResource(String resourceName) {
    InputStream resourceAsStream =
        checkNotNull(
            ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName),
            "Null resource content for " + resourceName);
    return new InputStreamReader(resourceAsStream);
  }

  private Migrator() {}
}
