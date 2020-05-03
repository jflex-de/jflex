/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.exceptions;

/**
 * This Exception is used in the macro expander to report cycles or undefined macro usages.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class MacroException extends Exception {

  /** */
  private static final long serialVersionUID = 275266242549067641L;

  /**
   * Creates a new MacroException with the specified message
   *
   * @param message the error description presented to the user.
   */
  public MacroException(String message) {
    super(message);
  }
}
