/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

/**
 * Signals a silent exit (no printStatistics printout).
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
public class SilentExit extends Exception {

  private static final int DEFAUT_EXIT_CODE = 1;
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
    this(DEFAUT_EXIT_CODE);
  }

  public SilentExit(String message) {
    super(message);
    exitCode = DEFAUT_EXIT_CODE;
  }

  public SilentExit(ErrorMessages messageFormat, Object... args) {
    this(ErrorMessages.get(messageFormat, args));
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
