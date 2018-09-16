package jflextest;

class InputOutput {

  private String name;
  private boolean outputExists;
  
  public InputOutput(String name, boolean outputExists) {
    this.outputExists = outputExists;
    this.name = name;
  }
  
  public String getName() { return name; }
  public boolean getOutputExists() { return outputExists; }
  
  public String toString() {
    return "Name:" + name + " OutputExists: " + outputExists;
  }  
}
