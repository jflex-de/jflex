/*
 * Copyright (C) 2022, Jesse Coultas
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/** Generator for Offset data values */
public class OffsetGen extends Generator<Integer> {
  public OffsetGen() {
    super(Integer.class);
  }

  @Override
  public Integer generate(SourceOfRandomness r, GenerationStatus status) {
    int rnd = r.nextInt(1, 100);

    // 5% change of getting number 0
    if (rnd >= 1 && rnd <= 5) {
      return 0;
    }

    // 5% change of getting number 1
    if (rnd >= 6 && rnd <= 10) {
      return 1;
    }

    // 5% change of getting Integer.MAX_VALUE
    if (rnd >= 11 && rnd <= 15) {
      return Integer.MAX_VALUE;
    }

    // 15% chance of getting a "larger" size
    if (rnd >= 16 && rnd <= 30) {
      return r.nextInt(200_001, 10_000_000);
    }

    // 5% chance of getting a "huge" size
    if (rnd >= 31 && rnd <= 35) {
      return r.nextInt(10_000_001, Integer.MAX_VALUE);
    }

    // 77% - normalish size
    return r.nextInt(100, 20_000);
  }
}
