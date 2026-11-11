/*
 * Copyright (C) 2022, Jesse Coultas
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import java.util.List;
import jflex.chars.Interval;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

/**
 * Generator for random characters as {@code int}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class IntCharGen {

  /** Characters up to {@link CharClasses#maxChar}, half with case options. */
  static Arbitrary<Integer> intChars() {
    return Arbitraries.oneOf(casedChars(), Arbitraries.integers().between(0, CharClasses.maxChar));
  }

  /** Characters with case options: a random interval of cased characters, then one of its chars. */
  static Arbitrary<Integer> casedChars() {
    List<Interval> casedIntervals;
    try {
      casedIntervals = new UnicodeProperties().getIntCharSet("cased").getIntervals();
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      throw new IllegalStateException(e);
    }
    return Arbitraries.of(casedIntervals)
        .flatMap(interval -> Arbitraries.integers().between(interval.start, interval.end));
  }

  private IntCharGen() {}
}
