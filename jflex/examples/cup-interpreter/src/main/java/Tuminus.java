/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** AST node for unary minus expressions. */
class Tuminus extends Texp implements AST {
  /** the negated expression */
  Texp exp;

  public Tuminus(Texp e) {
    exp = e;
  }

  public String toString() {
    return "-" + exp;
  }

  @Override
  public void checkcontext(SymTab st) {
    exp.checkcontext(st);
  }

  @Override
  public void prepInterp(SymTab st) {
    exp.prepInterp(st);
  }

  @Override
  public int interpret(int[] in, int[] par) {
    return -(exp.interpret(in, par));
  }
}
