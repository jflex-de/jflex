/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import jflex.chars.IntervalGen;
import jflex.logging.Out;

/**
 * Generator for random {@link IntCharSet} instances.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 * @see IntCharSet
 */
public class IntCharSetGen extends Generator<IntCharSet> {

  /** Min bound for number of intervals (0 = empty set) */
  private int minSize = 0;
  /** Max bound for number of intervals */
  private int maxSize = 5;

  /** Generator for Intervals */
  private final IntervalGen intervals;

  /** Constructs generator for IntCharSet */
  public IntCharSetGen() {
    super(IntCharSet.class);
    this.intervals = new IntervalGen();
  }

  @Override
  @SuppressWarnings("deprecation")
  public IntCharSet generate(SourceOfRandomness r, GenerationStatus status) {
    IntCharSet result = new IntCharSet();

    int numIntervals = r.nextInt(minSize, maxSize);
    for (int i = 0; i < numIntervals; i++) {
      result.add(intervals.generate(r, status));
    }

    // randomly add possible additional cased character
    if (numIntervals < maxSize && r.nextBoolean()) {
      try {
        result.add(IntCharGen.getRandomCased(r));
      } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
        Out.warning("Unable to fetch a random cased value - " + e.getMessage());
      }
    }

    return result;
  }

  /**
   * Configure this generator to only produce intervals in the given range.
   *
   * @param range annotation that contains the intervals constraints
   */
  public void configure(InRange range) {
    intervals.configure(range);
  }

  /**
   * Configure this generator to only produce IntCharSets with a given range of number of intervals.
   *
   * @param size annotation that contains how many intervals the IntCharSet should contain at least
   *     and at most
   */
  public void configure(Size size) {
    minSize = size.min();
    maxSize = size.max();
  }
}
