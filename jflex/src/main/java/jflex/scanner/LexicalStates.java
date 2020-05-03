/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.scanner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple symbol table, mapping lexical state names to integers.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class LexicalStates {

  /** maps state name to state number */
  Map<String, Integer> states;

  /** codes of inclusive states (subset of states) */
  List<Integer> inclusive;

  /** number of declared states */
  int numStates;

  /** Constructs a new lexical state symbol table. */
  public LexicalStates() {
    states = new LinkedHashMap<>();
    inclusive = new ArrayList<>();
  }

  /**
   * Inserts a new state declaration.
   *
   * @param name a {@link java.lang.String} object.
   * @param is_inclusive a boolean.
   */
  public void insert(String name, boolean is_inclusive) {
    if (states.containsKey(name)) return;

    Integer code = numStates++;
    states.put(name, code);

    if (is_inclusive) inclusive.add(code);
  }

  /**
   * Returns the number (code) of a declared state, {@code null} if no such state has been declared.
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link java.lang.Integer} object.
   */
  public Integer getNumber(String name) {
    return states.get(name);
  }

  /**
   * Returns the number of declared states.
   *
   * @return the number of declared states.
   */
  public int number() {
    return numStates;
  }

  /**
   * Returns the names of all states.
   *
   * @return the names of all states.
   */
  public Set<String> names() {
    return states.keySet();
  }

  /**
   * Returns the code of all inclusive states.
   *
   * @return the code of all inclusive states.
   */
  public List<Integer> getInclusiveStates() {
    return inclusive;
  }
}
