/*
 * Copyright (C) 2022, Jesse Coultas
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.List;
import jflex.chars.Interval;
import jflex.logging.Out;

/**
 * Generator for random Integer values that ensure to sometimes generate a cased character
 *
 * @author Jesse Coultas
 * @version JFlex 1.9.1
 */
public class IntCharGen extends Generator<Integer> {
  /** Constructs generator for CharClasses */
  public IntCharGen() throws UnicodeProperties.UnsupportedUnicodeVersionException {
    super(Integer.class);
  }

  @Override
  @SuppressWarnings("deprecation")
  public Integer generate(SourceOfRandomness r, GenerationStatus status) {
    // ensure we sometimes generate an int that has case options
    if (r.nextBoolean()) {
      try {
        return getRandomCased(r);
      } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
        Out.warning("Unable to fetch a random cased value - " + e.getMessage());
      }
    }

    return r.nextInt(0, CharClasses.maxChar);
  }

  public static Integer getRandomCased(SourceOfRandomness r)
      throws UnicodeProperties.UnsupportedUnicodeVersionException {
    // get list of casedIntervals
    List<Interval> casedIntervals = (new UnicodeProperties()).getIntCharSet("cased").getIntervals();

    // randomly pick an interval
    Interval interval = casedIntervals.get(r.nextInt(0, casedIntervals.size() - 1));

    // return a value between start and end of interval
    return r.nextInt(interval.start, interval.end);
  }
}
