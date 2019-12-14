/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * AST node for function application.
 *
 * <p>Also contains pointer to declaration location of the function.
 */
class Tfun extends Texp implements AST {
  Tident ident; // name of the function
  Texplist explist; // parameter list

  public Tfun(Tident i, Texplist e) {
    ident = i;
    explist = e;
  }

  public String toString() {
    return ident + "(" + explist + ")";
  }

  @Override
  public void checkcontext(SymTab st) { // CoCo (DefFun,Arity)
    explist.checkcontext(st);
    SymtabEntry ste = st.lookup(ident.toString());
    if (ste == null) Main.error("function not defined: " + ident);
    else if (ste.kind() != SymtabEntry.FUN) Main.error("variable used as funktion: " + ident);
    else if (((STEfun) ste).arity() != explist.length())
      Main.error("wrong arity at function call: " + ident);
  }

  Tdekl fundekl; // pointer to location of function declaration

  // set pointers and indices
  @Override
  public void prepInterp(SymTab st) {
    fundekl = ((STEfun) st.lookup(ident.toString())).getDekl();
    explist.prepInterp(st);
  }

  @Override
  public int interpret(int[] in, int[] par) {
    int[] newparams = new int[fundekl.arity()];
    explist.interpret(in, par, newparams, 0);
    return fundekl.interpret(in, newparams);
  }
}
