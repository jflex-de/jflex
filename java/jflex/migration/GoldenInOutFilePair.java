package jflex.migration;

import java.io.File;

public class GoldenInOutFilePair {

  final String name;
  final File inputFile;
  final File outputFile;

  GoldenInOutFilePair(String name, File inputFile, File outputFile) {
    this.name = name;
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  public String getName() {
    return name;
  }

  public String getInputFileName() {
    return inputFile.getName();
  }

  public String getOutputFileName() {
    return outputFile.getName();
  }

  public String toString() {
    return "Name:" + name;
  }
}
