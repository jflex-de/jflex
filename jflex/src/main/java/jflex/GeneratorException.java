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
 * Thrown when code generation has to be aborted.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
// TODO(regisd) Replace RuntimeException by checked exception
public class GeneratorException extends RuntimeException {

  private static final long serialVersionUID = -9128247888544263982L;

  public GeneratorException() {
    super();
  }

  public GeneratorException(ErrorMessages messageFormat, Object... args) {
    super(ErrorMessages.get(messageFormat, args));
  }

  public GeneratorException(Throwable cause, ErrorMessages messageFormat, Object... args) {
    super(ErrorMessages.get(messageFormat, args), cause);
  }

  public GeneratorException(Throwable cause) {
    super(cause);
  }
}
