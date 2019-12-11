/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core.unicode;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import jflex.chars.Interval;

/**
 * Generator for random {@link IntCharSet} instances.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 * @see IntCharSet
 */
public class IntCharSetGen extends Generator<IntCharSet> {

  /** Min bound for intervals */
  private int minChar = 0;
  /** Max bound for intervals. Small for speed, and more likely edge cases. */
  private int maxChar = 50;

  /** Min bound for number of intervals (0 = empty set) */
  private int minSize = 0;
  /** Max bound for number of intervals */
  private int maxSize = 5;

  /** Constructs generator for IntCharSet */
  public IntCharSetGen() {
    super(IntCharSet.class);
  }

  @Override
  public IntCharSet generate(SourceOfRandomness r, GenerationStatus status) {
    IntCharSet result = new IntCharSet();

    int numIntervals = r.nextInt(minSize, maxSize);
    for (int i = 0; i < numIntervals; i++) {
      int start = r.nextInt(minChar, maxChar);
      int end = r.nextInt(start, maxChar);

      // pick default with higher probability
      switch (r.nextInt(0, 4)) {
        case 0:
          result.add(IntCharSet.ofCharacter(start));
          break;
        case 1:
          result.add(start);
          break;
        default:
          result.add(new Interval(start, end));
          break;
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
    minChar = Math.max(0, range.minInt());
    maxChar = Math.min(range.maxInt(), CharClasses.maxChar);
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
