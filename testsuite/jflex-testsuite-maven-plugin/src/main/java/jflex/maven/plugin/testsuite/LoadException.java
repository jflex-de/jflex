/*
 * Copyright 2019, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.testsuite;

public class LoadException extends Exception {

  private static final long serialVersionUID = -8688118886747145114L;

  public LoadException(String message) {
    super(message);
  }
}
