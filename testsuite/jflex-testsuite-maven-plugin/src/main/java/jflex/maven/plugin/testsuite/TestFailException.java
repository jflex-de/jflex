package jflex.maven.plugin.testsuite;

public class TestFailException extends Exception {

  private static final long serialVersionUID = 8774936517110425532L;

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
