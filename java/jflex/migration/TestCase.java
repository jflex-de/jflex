package jflex.migration;

import java.util.List;
import java.util.Locale;

public class TestCase {

  /** Single common input file for all outputs */
  private String commonInputFile;

  private String className;

  /** The test name, usually lower case. */
  private String testName;

  private String description;

  private boolean expectJavacFail, expectJFlexFail;

  /** get- set- methods */
  void setTestName(String s) {
    testName = s;
    // TODO(regisd): The class name should depend on the flex `%class`, not on the test name.
    className = testName.substring(0, 1).toUpperCase(Locale.ENGLISH) + testName.substring(1);
  }

  void setJFlexDiff(List<Integer> d) {}

  void setDescription(String s) {
    description = s;
  }

  void setExpectJavacFail(boolean b) {
    expectJavacFail = b;
  }

  void setExpectJFlexFail(boolean b) {
    expectJFlexFail = b;
  }

  void setJflexCmdln(List<String> v) {}

  void setJavacFiles(List<String> v) {}

  void setInputFileEncoding(String e) {}

  void setOutputFileEncoding(String e) {}

  void setCommonInputFile(String f) {
    commonInputFile = f;
  }

  void setJavaVersion(String v) {}

  void setJavacEncoding(String v) {}

  public String getCommonInputFile() {
    return commonInputFile;
  }

  public String getClassName() {
    return className;
  }

  public String getTestName() {
    return testName;
  }

  public String getDescription() {
    return description;
  }

  public boolean isExpectJavacFail() {
    return expectJavacFail;
  }

  public boolean isExpectJFlexFail() {
    return expectJFlexFail;
  }

  @Override
  public String toString() {
    return testName;
  }
}
