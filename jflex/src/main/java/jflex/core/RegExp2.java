/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import jflex.logging.Out;

/**
 * Regular expression with two children (e.g. a | b)
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class RegExp2 extends RegExp {

  RegExp r1;
  RegExp r2;

  /**
   * Constructor for RegExp2.
   *
   * @param type a int.
   * @param r1 a {@link RegExp} object.
   * @param r2 a {@link RegExp} object.
   */
  public RegExp2(int type, RegExp r1, RegExp r2) {
    super(type);
    this.r1 = r1;
    this.r2 = r2;
  }

  @Override
  public String print(String tab) {
    return tab
        + "type = "
        + typeName()
        + Out.NL
        + tab
        + "child 1 :"
        + Out.NL
        + r1.print(tab + "  ")
        + Out.NL
        + tab
        + "child 2 :"
        + Out.NL
        + r2.print(tab + "  ");
  }

  @Override
  public String toString() {
    return print("");
  }
}
