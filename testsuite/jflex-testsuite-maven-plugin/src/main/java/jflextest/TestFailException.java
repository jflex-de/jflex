package jflextest;

public class TestFailException extends Exception {

  public TestFailException() {
    super();
  }

  public TestFailException(String message) {
    super(message);
  }

  public TestFailException(String message, Throwable cause) {
    super(message, cause);
  }
}
