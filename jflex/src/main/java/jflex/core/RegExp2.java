/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import jflex.env.Env;

/**
 * Regular expression with two children (e.g. a | b)
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
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
        + Env.NL
        + tab
        + "child 1 :"
        + Env.NL
        + r1.print(tab + "  ")
        + Env.NL
        + tab
        + "child 2 :"
        + Env.NL
        + r2.print(tab + "  ");
  }

  @Override
  public String toString() {
    return print("");
  }
}
