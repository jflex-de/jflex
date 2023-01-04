/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/** AST node for bool expressions */
class Tboolexp implements AST {
  /** Left subexpression. */
  Texp exp1;
  /** Right subexpression. */
  Texp exp2;
  /** {@code '='}, {@code '<'} and {@code '!'} for "<=". */
  char kind;

  public Tboolexp(Texp e1, char k, Texp e2) {
    exp1 = e1;
    kind = k;
    exp2 = e2;
  }

  public String toString() {
    if (kind != '!') return ("" + exp1 + kind + exp2);
    else return (exp1 + "<=" + exp2);
  }

  public void checkcontext(SymTab st) { // context conditions
    exp1.checkcontext(st);
    exp2.checkcontext(st);
  }

  public void prepInterp(SymTab st) { // set pointers and indices
    exp1.prepInterp(st);
    exp2.prepInterp(st);
  }

  public boolean interpret(int[] in, int[] par) {
    int e1 = exp1.interpret(in, par);
    int e2 = exp2.interpret(in, par);
    switch (kind) {
      case '=':
        return (e1 == e2);
      case '<':
        return (e1 < e2);
      case '!':
        return (e1 <= e2);
    }

    return (false); // error
  }
}
