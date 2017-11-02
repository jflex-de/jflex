package jflextest;

/**
 * Stores the result (for now: output + success status) of a test run
 * of one single program (jflex, javac, generated scanner, whatever).  
 */
public class TestResult {

  private String output;
  private boolean success;

  public TestResult(String output, boolean success) {
    this.output = output;
    this.success = success;
  }

  public String getOutput() { 
    return output;
  }

  public void setOutput(String output) {
    this.output = output;  
  }	
  
  public boolean getSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }	

  public String toString() {
    return "success: "+success+"\noutput: \""+output+"\"";
  }
}
