/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/** AST node for an identifier */
class Tident extends Texp implements AST {
  String name;

  public Tident(String s) {
    name = s;
  }

  public String toString() {
    return name;
  }

  @Override
  public void checkcontext(SymTab st) { // CoCo (DefVar)
    SymtabEntry ste = st.lookup(name);

    if (ste == null) Main.error("variable not defined: " + name);
    else if (ste.kind() != SymtabEntry.VAR) Main.error("function used as variable: " + name);
  }

  int index; // number of ident in environment
  boolean is_input; // is it an input variable?

  @Override
  public void prepInterp(SymTab st) { // set index for environment
    STEvar ste = (STEvar) st.lookup(name);
    index = ste.getIndex();
    is_input = ste.isInput();
  }

  @Override
  public int interpret(int[] in, int[] par) {
    if (is_input) return (in[index]);
    else return (par[index]);
  }
}
