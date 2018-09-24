/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** AST node for declaration lists of functions */
class Tdekllist implements AST {
  /** rest list (optional null) */
  Tdekllist dekllist;
  /** declaration */
  Tdekl dekl;

  public Tdekllist(Tdekllist p, Tdekl e) {
    dekllist = p;
    dekl = e;
  }

  public Tdekllist(Tdekl e) {
    dekllist = null;
    dekl = e;
  }

  public String toString() {
    if (dekllist != null) return (dekllist + ",\n" + dekl);
    else return (dekl.toString());
  }

  public void setSymtab(SymTab st) {
    if (dekllist != null) dekllist.setSymtab(st);
    dekl.setSymtab(st);
  }

  public void printSymtabs() {
    if (dekllist != null) dekllist.printSymtabs();
    dekl.printSymtabs();
  }

  public void checkcontext() {
    if (dekllist != null) dekllist.checkcontext();
    dekl.checkcontext(); // CoCo (DefFun,DefVar,Arity)
  } // in function body

  public void prepInterp(SymTab st) { // set pointers and indices
    dekl.prepInterp(st);
    if (dekllist != null) dekllist.prepInterp(st);
  }
}
