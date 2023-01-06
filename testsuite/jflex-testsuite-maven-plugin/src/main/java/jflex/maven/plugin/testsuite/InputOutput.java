/*
 * Copyright 2019, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.testsuite;

class InputOutput {

  private String name;
  private boolean outputExists;

  public InputOutput(String name, boolean outputExists) {
    this.outputExists = outputExists;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean getOutputExists() {
    return outputExists;
  }

  @Override
  public String toString() {
    return "Name:" + name + " OutputExists: " + outputExists;
  }
}
