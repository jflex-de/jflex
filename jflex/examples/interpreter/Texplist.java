/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * AST node for a list of expressions.
 *
 * <p>The interpretation of a list of expressions stores the results of the expressions in an array
 * that can be used as parameter list for function calls.
 */
class Texplist implements AST {
  Texplist explist; // next list element (optional null)
  Texp exp; // expression of this list node

  public Texplist(Texplist p, Texp e) {
    explist = p;
    exp = e;
  }

  public Texplist(Texp e) {
    explist = null;
    exp = e;
  }

  public String toString() {
    if (explist != null) return explist + "," + exp;
    else return exp.toString();
  }

  public void checkcontext(SymTab st) {
    if (explist != null) explist.checkcontext(st);
    exp.checkcontext(st); // CoCo (DefFun,DefVar,Arity)
  } // in expression

  public int length() {
    if (explist != null) return 1 + explist.length();
    else return 1;
  }

  public void prepInterp(SymTab st) { // set pointers and indices
    exp.prepInterp(st);
    if (explist != null) explist.prepInterp(st);
  }

  public void interpret(int[] in, int[] par, int[] res, int index) {
    res[index] = exp.interpret(in, par);
    if (explist != null) explist.interpret(in, par, res, index + 1);
  }
}
