/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** AST node for infix expressions */
class Texpinfix extends Texp implements AST {
  Texp exp1, exp2; // left and right sub expression
  /** kind ('+', '-', '*', '/') */
  char kind;

  public Texpinfix(Texp e1, char k, Texp e2) {
    exp1 = e1;
    kind = k;
    exp2 = e2;
  }

  public String toString() {
    return ("(" + exp1 + kind + exp2 + ")");
  }

  @Override
  public void checkcontext(SymTab st) { // context conditions
    exp1.checkcontext(st);
    exp2.checkcontext(st);
  }

  @Override
  public void prepInterp(SymTab st) { // set pointers und indices
    exp1.prepInterp(st);
    exp2.prepInterp(st);
  }

  @Override
  public int interpret(int[] in, int[] par) {
    int e1 = exp1.interpret(in, par);
    int e2 = exp2.interpret(in, par);

    switch (kind) {
      case '+':
        return (e1 + e2);
      case '-':
        return (e1 - e2);
      case '*':
        return (e1 * e2);
      case '/':
        return (e1 / e2);
    }

    return -1; // error
  }
}
