/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DfaTest {

  @Test
  @SuppressWarnings("deprecation")
  public void copyOf() {
    DFA dfa1 = new DFA(2, 1, 1);
    DFA dfa2 = DeprecatedDfa.copyOf(dfa1);
    assertEquals(dfa1, dfa2);
  }
}
