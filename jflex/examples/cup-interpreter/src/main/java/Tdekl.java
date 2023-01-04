/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * AST node for function declarations.
 *
 * <p>Also contains a reference to the symbol table of the parameters and their arity.
 */
class Tdekl implements AST {
  /** identifier */
  Tident ident; //
  /** list of parameters */
  Tparlist parlist;
  /** function body */
  Texp exp;

  public Tdekl(Tident i, Tparlist p, Texp e) {
    parlist = p;
    ident = i;
    exp = e;
  }

  public String toString() {
    return (ident + "(" + parlist + ") = \n  " + exp);
  }

  SymTab params; // symbol table of the parameters
  int arity;

  public void setSymtab(SymTab st) {
    params = new SymTab(st);
    parlist.setSymtab(params, false, 0);
    arity = params.size();

    boolean isNew = st.enter(ident.toString(), new STEfun(ident.toString(), this, arity));
    // CoCo (Fun)
    if (!isNew) Main.error("funktion " + ident + " defined twice!");
  }

  public void printSymtabs() {
    System.out.print("funktion " + ident.toString() + "\n" + params);
  }

  public void checkcontext() {
    exp.checkcontext(params); // CoCo (DefFun,DefVar,Arity)
  }

  public void prepInterp(SymTab st) { // set pointers and indices
    exp.prepInterp(params);
  }

  public int interpret(int[] in, int[] par) {
    return (exp.interpret(in, par));
  }

  public int arity() {
    return (arity);
  }
}
