package de.jflex.testing.testsuite.golden;

import java.io.File;

public class GoldenInOutFilePair {

  public final File inputFile;
  public final File outputFile;

  public GoldenInOutFilePair(File inputFile, File outputFile) {
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  public String getInputFileName() {
    return inputFile.getName();
  }

  public String getOutputFileName() {
    return outputFile.getName();
  }

  @Override
  public String toString() {
    return inputFile.getParent() + File.separator + inputFile.getName();
  }
}
