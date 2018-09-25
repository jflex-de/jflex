/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.*;

/**
 * Symbol table for the interpreter, contains information about variables and functions.
 *
 * <p>For every binding location of a name a symbol will be created. The symbol tables are connected
 * hierarchically by pointers to the predecessor. Lookup takes predecessors into account.
 */
public class SymTab {
  /** contains the list of words. */
  Map<String, SymtabEntry> m;
  /** predecessor symbol table (if exists) */
  SymTab pred;

  public SymTab() {
    this(null);
  }

  public SymTab(SymTab p) {
    m = new HashMap<String, SymtabEntry>();
    pred = p;
  }

  public boolean enter(String s, SymtabEntry e) {
    Object value = lookup(s);
    m.put(s, e);
    return (value == null);
  }

  public SymtabEntry lookup(String s) {
    SymtabEntry value = m.get(s);
    if (value == null && pred != null) value = pred.lookup(s);
    return value;
  }

  public String toString() { // for output with print
    StringBuilder res = new StringBuilder("symbol table\n=============\n");

    for (Map.Entry<String, SymtabEntry> entry : m.entrySet())
      res.append(entry.getKey()).append("   \t").append(entry.getValue()).append("\n");

    if (pred != null) res.append("++ predecessor!\n");

    return res.toString();
  }

  public int size() {
    return (m.size());
  }
}
