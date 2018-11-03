/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.exceptions;

/**
 * Thrown when code generation has to be aborted.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
public class GeneratorException extends RuntimeException {

  private static final long serialVersionUID = -9128247888544263982L;

  public GeneratorException() {
    super("Generation aborted for an unknown reason");
  }

  public GeneratorException(Throwable cause) {
    super("Generation aborted: " + cause, cause);
  }
}
