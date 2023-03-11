/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.exceptions;

/**
 * Signals a silent exit (no statistics printout).
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class SilentExit extends Exception {

  /** Program exit code if this exception is taken */
  private final int exitCode;

  /**
   * SilentExit with specified program exit code.
   *
   * @param exitCode a int.
   */
  public SilentExit(int exitCode) {
    this.exitCode = exitCode;
  }

  /** SilentExit with default exit code 1. */
  public SilentExit() {
    this(1);
  }

  /**
   * The exit code of this SilentExit exception.
   *
   * @return a int.
   */
  public int exitCode() {
    return exitCode;
  }

  /** Serialisation */
  private static final long serialVersionUID = 8288632239818668902L;
}
