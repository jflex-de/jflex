/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Symbol table entry for variables.
 *
 * <p>Contains index in the parameter list and a flag if it is an input variable.
 */
class STEvar extends SymtabEntry {
  boolean is_input;
  int index;

  public STEvar(String v, boolean ii, int ind) {
    super(v);
    is_input = ii;
    index = ind;
  }

  @Override
  public int kind() {
    return SymtabEntry.VAR;
  }

  public String toString() {
    if (is_input) return "input var " + name + "  (" + index + ")";
    else return "parameter " + name + "  (" + index + ")";
  }

  public int getIndex() {
    return index;
  }

  public boolean isInput() {
    return is_input;
  }
}
