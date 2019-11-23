package jflex.testing.testsuite.golden;

import java.io.File;

public class GoldenInOutFilePair {

  public final String name;
  public final File inputFile;
  public final File outputFile;

  public GoldenInOutFilePair(String name, File inputFile, File outputFile) {
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
