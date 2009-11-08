package jflextest;

public class TestFailException extends Exception {
  
  public TestFailException() {
    this("");
  }

  public TestFailException(String message) {
    super(message);
  }

}
