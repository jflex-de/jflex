/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import jflex.dfa.DFA;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;

/**
 * Property-based tests for the {@link Emitter}.
 *
 * @see Emitter
 */
public class EmitterQuickcheck {

  /**
   * {@link Emitter#reduceRows(DFA, int[], boolean[])} gives the same result as the previous
   * implementation, which compares each row with all rows before it.
   */
  @Property
  @StatisticsReport(StatisticsReport.StatisticsReportMode.OFF)
  public void reduceRowsAgreesWithPairwiseComparison(@ForAll DFA dfa) {
    int[] expectedMap = new int[dfa.numStates()];
    boolean[] expectedKilled = new boolean[dfa.numStates()];
    reduceRowsPairwise(dfa, expectedMap, expectedKilled);

    // DFAs with and without equal rows have to occur for this property to mean something
    boolean equalRows = false;
    for (boolean killed : expectedKilled) {
      equalRows |= killed;
    }
    Statistics.collect(equalRows);
    Statistics.coverage(
        coverage -> {
          coverage.check(true).percentage(p -> p > 10);
          coverage.check(false).percentage(p -> p > 10);
        });

    int[] rowMap = new int[dfa.numStates()];
    boolean[] rowKilled = new boolean[dfa.numStates()];
    Emitter.reduceRows(dfa, rowMap, rowKilled);

    assertArrayEquals(expectedMap, rowMap);
    assertArrayEquals(expectedKilled, rowKilled);
  }

  /**
   * The implementation of {@code Emitter.reduceRows} before it used a hash map. Now as test oracle.
   * Quadratic in the number of states.
   */
  private static void reduceRowsPairwise(DFA dfa, int[] rowMap, boolean[] rowKilled) {
    int i, j, k;
    int translate = 0;
    boolean equal;

    // i is the state to add to the new table
    for (i = 0; i < dfa.numStates(); i++) {

      rowMap[i] = i - translate;

      // check if state i can be removed (i.e. already
      // exists in entries 0..i-1)
      for (j = 0; j < i; j++) {

        // test for equality:
        k = -1;
        equal = true;
        while (equal && ++k < dfa.numInput()) equal = dfa.table(i, k) == dfa.table(j, k);

        if (equal) {
          translate++;
          rowMap[i] = rowMap[j];
          rowKilled[i] = true;
          break;
        } // if
      } // for j
    } // for i
  }
}
