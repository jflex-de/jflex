package jflex.maven.plugin.testsuite;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.apache.maven.plugin.MojoFailureException;

public class TestCase {

  /** command line switches for jflex invocation */
  private List<String> jflexCmdln = new ArrayList<>();

  /** files on which to invoke jflex */
  private List<String> jflexFiles;

  /** command line switches for javac invocation, instead of the default {@code <testname>.java}. */
  private List<String> javacFiles;

  /** lines with expected differences in jflex output */
  private List<Integer> jflexDiff;

  /** Test input file encoding -- defaults to UTF-8 */
  private String inputFileEncoding = "UTF-8";

  /** Test output file encoding -- defaults to UTF-8 */
  private String outputFileEncoding = "UTF-8";

  /** Single common input file for all outputs */
  private String commonInputFile = null;

  /** Java version this test case is intended for */
  private String javaVersion = System.getProperty("java.version");

  /** misc. */
  private String className;

  private String testName;
  private String description;

  /** base directory of this test case */
  private File testPath;

  private boolean expectJavacFail, expectJFlexFail;

  /** inputOutputFiles to invoke test.main on and compare */
  private List<InputOutput> inputOutput;

  /** encoding to use for compiling the .java file; defaults to UTF-8 */
  private String javacEncoding = "UTF-8";

  /** get- set- methods */
  void setTestName(String s) {
    testName = s;
    // TODO(regisd): The class name should depend on the flex `%class`, not on the test name.
    className = testName.substring(0, 1).toUpperCase(Locale.ENGLISH) + testName.substring(1);
  }

  void setJFlexDiff(List<Integer> d) {
    jflexDiff = d;
  }

  void setDescription(String s) {
    description = s;
  }

  void setExpectJavacFail(boolean b) {
    expectJavacFail = b;
  }

  void setExpectJFlexFail(boolean b) {
    expectJFlexFail = b;
  }

  void setJflexCmdln(List<String> v) {
    jflexCmdln = v;
  }

  void setJavacFiles(List<String> v) {
    javacFiles = v;
  }

  private void setInputOutput(List<InputOutput> v) {
    inputOutput = v;
  }

  void setInputFileEncoding(String e) {
    inputFileEncoding = e;
  }

  void setOutputFileEncoding(String e) {
    outputFileEncoding = e;
  }

  void setCommonInputFile(String f) {
    commonInputFile = f;
  }

  void setJavaVersion(String v) {
    this.javaVersion = v;
  }

  void setJavacEncoding(String v) {
    this.javacEncoding = v;
  }

  public TestCase() {
    jflexFiles = new ArrayList<>();
  }

  boolean checkJavaVersion() {
    String current = System.getProperty("java.version");
    return current.startsWith(javaVersion);
  }

  void init(File testDir) {
    // get files to process
    List<InputOutput> temp = new ArrayList<>();
    String name;
    for (String file : testDir.list()) {
      if (null != commonInputFile) {
        if (Objects.equals(file, testName + ".output")) {
          temp.add(new InputOutput((new File(testDir, testName)).toString(), true));
          commonInputFile = (new File(testDir, commonInputFile)).toString();
        }
      } else {
        if (file.endsWith(".input") && file.startsWith(testName + "-")) {
          name = file.substring(0, file.length() - 6);
          temp.add(
              new InputOutput(
                  (new File(testDir, name)).toString(),
                  new File(testDir, name + ".output").exists()));
        }
      }
    }
    // When a common input file is specified, but no output file exists, add
    // a single InputOutput for the common input file.
    if (null != commonInputFile && 0 == temp.size()) {
      temp.add(new InputOutput(null, false));
    }
    setInputOutput(temp);
    testPath = testDir;
  }

  void createScanner(File jflexUberJar, boolean verbose)
      throws TestFailException, MojoFailureException {
    File expected = new File(testPath, testName + "-flex.output");
    jflexFiles.add((new File(testPath, testName + ".flex")).getPath());
    // invoke JFlex
    TestResult jflexResult = Exec.execJFlex(jflexCmdln, jflexFiles);
    // System.out.println(jflexResult);

    if (jflexResult.getSuccess()) {
      if (expectJFlexFail) {
        throw new TestFailException("JFlex failure expected, but it succeeded");
      }

      // check JFlex output conformance
      checkOutput(jflexResult.getOutput(), expected);

      // Compile Scanner
      final List<String> toCompile = getFilesToCompile();
      if (Tester.verbose) {
        System.out.println("File(s) to compile: " + toCompile);
      }
      try {
        TestResult javacResult = Exec.execJavac(toCompile, testPath, jflexUberJar, "UTF-8");

        // System.out.println(javacResult);
        if (Tester.verbose) {
          System.out.println(
              "Compilation successful: "
                  + javacResult.getSuccess()
                  + " [expected: "
                  + !expectJavacFail
                  + "]");
        }
        if (javacResult.getSuccess() == expectJavacFail) {
          throw new TestFailException(
              "Compilation failed in " + testPath + " for " + toCompile,
              new Exception(javacResult.getOutput()));
        }
      } catch (FileNotFoundException e) {
        throw new TestFailException("javac: file not found: ", e);
      }
    } else {
      if (!expectJFlexFail) {
        System.out.println("Scanner generation failed!");
        System.out.println("JFlex output was:\n" + jflexResult.getOutput());
        throw new TestFailException("Scanner generation failed");
      } else {
        checkOutput(jflexResult.getOutput(), expected);
      }
    }
  }

  /** Check JFlex output conformance */
  private void checkOutput(String actual, File goldenFile) throws TestFailException {
    if (goldenFile.exists()) {
      DiffStream check = new DiffStream();
      String diff;
      try {
        diff = check.diff(jflexDiff, new StringReader(actual), new FileReader(goldenFile));
      } catch (FileNotFoundException e) {
        System.out.println("Error opening file " + goldenFile);
        throw new TestFailException();
      }
      if (diff != null) {
        System.err.println("Test failed, unexpected jflex output: " + diff);
        System.out.println("Expected content of: " + goldenFile.getAbsolutePath());
        try {
          {
            File file = File.createTempFile(testName, ".actual");
            try (FileWriter writer = new FileWriter(file)) {
              writer.write(actual);
              System.out.println("Actual JFlex saved in: " + file.getAbsolutePath());
            }
          }
        } catch (IOException e) {
          throw new TestFailException(e.getMessage());
        }
        throw new TestFailException();
      }
    } else {
      System.err.println("Warning: Golden file [" + goldenFile + "] missing");
    }
  }

  /** Returns the list of java files to compile. */
  private List<String> getFilesToCompile() {
    if (javacFiles == null) {
      return ImmutableList.of(new File(testPath, className + ".java").getName());
    } else {
      ImmutableList.Builder<String> builder = ImmutableList.builder();
      for (String explicitJavaSrc : javacFiles) {
        File f = new File(testPath, explicitJavaSrc);
        builder.add(f.getName());
      }
      return builder.build();
    }
  }

  boolean hasMoreToDo() {
    // check if there's more files to run scanner main on
    return !(inputOutput.isEmpty());
  }

  void runNext(File jflexUberJar) throws TestFailException, UnsupportedEncodingException {
    // Get first file and remove it from list
    InputOutput current = inputOutput.remove(0);
    // Create List with only first input in
    List<String> inputFiles = new ArrayList<>();
    inputFiles.add(null != commonInputFile ? commonInputFile : current.getName() + ".input");
    // Execute Tester on that input
    List<String> cmdLine = new ArrayList<>();
    cmdLine.add("--encoding");
    cmdLine.add(inputFileEncoding);
    List<File> additionalJars = ImmutableList.of(jflexUberJar);
    TestResult classExecResult =
        Exec.execClass(
            className,
            testPath.toString(),
            inputFiles,
            additionalJars,
            outputFileEncoding,
            cmdLine);

    // check for output conformance
    checkOutput(classExecResult.getOutput(), new File(current.getName() + ".output"));
  }

  public String toString() {
    return "Test name: "
        + testName
        + "\nDescription: "
        + description
        + "JFlexFail: "
        + expectJFlexFail
        + " JavacFail: "
        + expectJavacFail
        + "\n"
        + "JFlex Command line: "
        + jflexCmdln
        + (null != javacFiles ? " Javac Files: " + Arrays.toString(javacFiles.toArray()) : "")
        + "\n"
        + "Files to run Tester on "
        + inputOutput
        + (null != commonInputFile ? " Common input file: " + commonInputFile : "")
        + "Java version "
        + javaVersion;
  }
}
