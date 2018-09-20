/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

/**
 * This Exception is used in class CharClasses.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class CharClassException extends RuntimeException {

  private static final long serialVersionUID = 7199804506062103569L;

  public CharClassException() {}

  /**
   * Creates a new CharClassException with the specified message
   *
   * @param message the error description presented to the user.
   */
  public CharClassException(String message) {
    super(message);
  }
}
