/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/** AST node for a number */
class Tnumber extends Texp implements AST {
  int n; // value of the number

  public Tnumber(String s) {
    try {
      n = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      n = -1;
    }
  }

  public String toString() {
    return String.valueOf(n);
  }

  @Override
  public void checkcontext(SymTab st) {}

  @Override
  public void prepInterp(SymTab st) {}

  @Override
  public int interpret(int[] in, int[] par) {
    return n;
  }
}
