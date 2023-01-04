/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Symbol table entry for names, there are subclasses for variables and functions.
 *
 * <p>Defines constants UNKNOWN, VAR and FUN as kinds of symbol table entries.
 */
class SymtabEntry {
  String name;

  public SymtabEntry(String v) {
    name = v;
  }

  public int kind() {
    return UNKNOWN;
  }

  public String toString() {
    return ("unknown " + name);
  }

  static final int UNKNOWN = 12;
  static final int VAR = 13;
  static final int FUN = 14;
}
