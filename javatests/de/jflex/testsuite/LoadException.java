/*
 * Copyright 2019, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testsuite;

/** A {@code .test} file that {@link TestLoader} cannot read. */
public class LoadException extends Exception {

  private static final long serialVersionUID = 1L;

  public LoadException(String message) {
    super(message);
  }
}
