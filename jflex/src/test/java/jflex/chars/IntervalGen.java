/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.chars;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import jflex.core.unicode.CharClasses;

/**
 * Generator for random {@link Interval} instances.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 * @see Interval
 */
public class IntervalGen extends Generator<Interval> {

  /** Min bound for intervals */
  private int minChar = 0;
  /** Max bound for intervals. Small for speed, and more likely edge cases. */
  private int maxChar = 50;

  /** How often to return single-character intervals. */
  private final float singleCharRatio = 0.2f;

  /** Constructs and registers generator for Interval */
  public IntervalGen() {
    super(Interval.class);
  }

  @Override
  public Interval generate(SourceOfRandomness random, GenerationStatus status) {
    int a = random.nextInt(minChar, maxChar);
    if (random.nextFloat(0, 1) > singleCharRatio) {
      int b = random.nextInt(minChar, maxChar);
      return a <= b ? new Interval(a, b) : new Interval(b, a);
    } else {
      return Interval.ofCharacter(a);
    }
  }

  /**
   * Configure this generator to only produce intervals in the given range.
   *
   * @param range annotation that contains the intervals constraints
   */
  public void configure(InRange range) {
    minChar = Math.max(0, range.minInt());
    maxChar = Math.min(range.maxInt(), CharClasses.maxChar);
  }
}
