package jflex.migration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TestCase {

  /** command line switches for jflex invocation */
  private List<String> jflexCmdln;

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
