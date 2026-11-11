/*
 * Copyright (C) 2022, Jesse Coultas
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Tuple;

/** Generator for Offset data values */
public class OffsetGen {

  static Arbitrary<Integer> offsets() {
    return Arbitraries.frequencyOf(
        Tuple.of(5, Arbitraries.just(0)),
        Tuple.of(5, Arbitraries.just(1)),
        Tuple.of(5, Arbitraries.just(Integer.MAX_VALUE)),
        // a "larger" size
        Tuple.of(15, Arbitraries.integers().between(200_001, 10_000_000)),
        // a "huge" size
        Tuple.of(5, Arbitraries.integers().between(10_000_001, Integer.MAX_VALUE)),
        // normalish size
        Tuple.of(65, Arbitraries.integers().between(100, 20_000)));
  }

  private OffsetGen() {}
}
