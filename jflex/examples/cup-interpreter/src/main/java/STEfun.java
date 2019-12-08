/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * Symbol table entry for functions.
 *
 * <p>Contains arity and reference to location of definition
 */
class STEfun extends SymtabEntry {
  int arity;
  Tdekl dekl; // location of definition

  public STEfun(String f, Tdekl d, int a) {
    super(f);
    dekl = d;
    arity = a;
  }

  @Override
  public int kind() {
    return SymtabEntry.FUN;
  }

  public String toString() {
    return "function    " + name + ", arity " + arity;
  }

  public int arity() {
    return arity;
  }

  public Tdekl getDekl() {
    return dekl;
  }
}
