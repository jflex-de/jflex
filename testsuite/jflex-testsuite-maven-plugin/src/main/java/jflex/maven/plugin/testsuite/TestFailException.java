package jflex.maven.plugin.testsuite;

public class TestFailException extends Exception {

  public TestFailException() {
    this("");
  }

  public TestFailException(String message) {
    super(message);
  }

  public TestFailException(String message, Throwable cause) {
    super(message, cause);
  }
}
