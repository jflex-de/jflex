/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

/**
 * This exception is used for unexpected errors in in regexp recursion, such as unexpected
 * expression type or structure.
 *
 * <p>If this is encountered, this means there is a bug.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
// TODO Move all regexp related code to jflex.regexp
// This class lives in package jflex.core, because the reference to jflex.core.RegExp would
// introduce a cyclic module dependency in jflex.exceptions.
public class RegExpException extends RuntimeException {

  /** Required by serialisation interface */
  private static final long serialVersionUID = -1072445391741394972L;

  /**
   * Creates a new RegExpException with the specified message
   *
   * @param message the error description presented to the user.
   */
  private RegExpException(String message) {
    super(message);
  }

  /**
   * Creates a new RegExpException for the specified regular expression.
   *
   * @param e the regexp that caused this exception.
   */
  public RegExpException(RegExp e) {
    this("Unexpected regexp " + e);
  }
}
