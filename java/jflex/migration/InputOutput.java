package jflex.migration;

class InputOutput {

  final String name;
  final boolean outputExists;

  InputOutput(String name, boolean outputExists) {
    this.outputExists = outputExists;
    this.name = name;
  }

  public String toString() {
    return "Name:" + name + " OutputExists: " + outputExists;
  }
}
