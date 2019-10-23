/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.exceptions;

/**
 * This exception is used for unexpected errors in in regexp recursion, such as unexpected
 * expression type or structure.
 *
 * <p>If this is encountered, this means there is a bug.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class RegExpException extends RuntimeException {

  /** Required by serialisation interface */
  private static final long serialVersionUID = -1072445391741394972L;

  /**
   * Creates a new RegExpException with the specified message
   *
   * @param message the error description presented to the user.
   */
  public RegExpException(String message) {
    super(message);
  }
}
