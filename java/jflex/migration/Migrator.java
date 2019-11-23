package jflex.migration;

import static com.google.common.collect.ImmutableList.toImmutableList;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import jflex.util.javac.JavaPackageUtil;
import jflex.velocity.Velocity;
import org.apache.velocity.runtime.parser.ParseException;

public class Migrator {

  private static final String TEST_CASE_TEMPLATE =
      JavaPackageUtil.getPathForClass(Migrator.class) + "/TestCase.java.vm";

  private static FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) {
    Preconditions.checkArgument(args.length > 0, "Syntax error: migrator TESTCASE_DIRS_ABS_PATH");
    try {
      for (String testCase : args) {
        migrateCase(testCase);
      }
    } catch (MigrationException e) {
      logger.atSevere().log("Migration failed", e);
    }
  }

  private static void migrateCase(String testCase) throws MigrationException {
    logger.atInfo().log("Migrating %s...", testCase);
    File dir = new File(testCase);
    if (!dir.exists()) {
      logger.atWarning().log("Directory doesn't exist: " + dir.getName());
      throw new MigrationException(
          "Could not migrate " + testCase, new FileNotFoundException(dir.getAbsolutePath()));
    }
    migrateCase(dir);
  }

  private static void migrateCase(File testCaseDir) throws MigrationException {
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
      migrateTestCase(testCaseDir, test);
    } catch (IOException e) {
      throw new MigrationException("Failed reading the test spec " + testSpecFile.getName(), e);
    }
  }

  private static void migrateTestCase(File testCaseDir, TestCase test) throws MigrationException {
    TestCaseVars templateVars = createTemplateVars(testCaseDir, test);
    try {
      render(templateVars, System.out);
    } catch (IOException e) {
      throw new MigrationException("Couldn't write java test case", e);
    }
  }

  private static TestCaseVars createTemplateVars(File testCaseDir, TestCase test) {
    TestCaseVars vars = new TestCaseVars();
    vars.javaPackage = "org.example";
    vars.testCaseName = "HelloTest";
    return vars;
  }

  private static void render(TestCaseVars templateVars, OutputStream output)
      throws IOException, MigrationException {
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
      Velocity.render(readResource(TEST_CASE_TEMPLATE), "UnicodeProperties", templateVars, writer);
    } catch (ParseException e) {
      throw new MigrationException("Failed to parse Velocity template " + TEST_CASE_TEMPLATE, e);
    }
  }

  private static InputStreamReader readResource(String resourceName) {
    return new InputStreamReader(
        ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName));
  }

  private Migrator() {}
}
