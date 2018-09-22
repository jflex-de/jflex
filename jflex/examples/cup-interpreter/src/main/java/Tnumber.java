/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


/**
 * AST node for a number
 */ 
class Tnumber extends Texp implements AST {
  int n;             // value of the number

  public Tnumber(String s) {
    try { n = Integer.parseInt(s); }
    catch (NumberFormatException e) { n=-1; };
  }

  public String toString() {
    return(""+n); 
  }

  public void checkcontext(SymTab st) { 
  }

  public void prepInterp(SymTab st) { 
  }

  public int interpret(int[] in, int[] par) {
    return(n);
  }
}

