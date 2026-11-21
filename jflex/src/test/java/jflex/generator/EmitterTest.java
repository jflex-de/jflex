/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import jflex.dfa.DFA;
import jflex.option.Options;
import org.junit.jupiter.api.Test;

/**
 * Some unit tests for the jflex Emitter class
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class EmitterTest {
  @Test
  public void testSourceFileString() {
    Options options = new Options();
    String bad = "something/or_other\\filename\\nFILE_NAMES_MUST_BE_ESCAPED\\u000A.flex";
    if (File.separatorChar == '\\') {
      assertEquals(
          "something/or_other/filename/nFILE_NAMES_MUST_BE_ESCAPED/u000A.flex",
          Emitter.sourceFileString(new File(bad), options));
    } else {
      assertEquals(
          "something/or_other\\\\filename\\\\nFILE_NAMES_MUST_BE_ESCAPED\\\\u000A.flex",
          Emitter.sourceFileString(new File(bad), options));
    }
  }

  @Test
  public void reduceRows() {
    DFA dfa = new DFA(2, 2, 1);
    // state 0: 1, none
    dfa.addTransition(0, 0, 1);
    // state 1: 2, 2
    dfa.addTransition(1, 0, 2);
    dfa.addTransition(1, 1, 2);
    // state 2: 1, none
    dfa.addTransition(2, 0, 1);
    // state 3: 2, 2
    dfa.addTransition(3, 0, 2);
    dfa.addTransition(3, 1, 2);
    // state 4: none, 5
    dfa.addTransition(4, 1, 5);
    // state 5: none, none
    // state 6: none, 5
    dfa.addTransition(6, 1, 5);

    int[] rowMap = new int[dfa.numStates()];
    boolean[] rowKilled = new boolean[dfa.numStates()];
    Emitter.reduceRows(dfa, rowMap, rowKilled);

    assertArrayEquals(new int[] {0, 1, 0, 1, 2, 3, 2}, rowMap);
    assertArrayEquals(new boolean[] {false, false, true, true, false, false, true}, rowKilled);
  }
}
