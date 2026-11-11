/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.chars;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import jflex.core.unicode.CharClasses;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Tuple;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

/**
 * Generator for random {@link Interval} instances.
 *
 * <p>{@link IntRange} on the parameter bounds the intervals.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see Interval
 */
public class IntervalGen implements ArbitraryProvider {

  /** Default max bound for intervals. Small for speed, and more likely edge cases. */
  public static final int MAX_CHAR = 50;

  @Override
  public boolean canProvideFor(TypeUsage targetType) {
    return targetType.isOfType(Interval.class);
  }

  @Override
  public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
    return Collections.singleton(intervals(targetType.findAnnotation(IntRange.class)));
  }

  /** Intervals in {@code range}, by default from 0 to {@link #MAX_CHAR}. */
  public static Arbitrary<Interval> intervals(Optional<IntRange> range) {
    return intervals(
        range.map(r -> Math.max(0, r.min())).orElse(0),
        range.map(r -> Math.min(r.max(), CharClasses.maxChar)).orElse(MAX_CHAR));
  }

  /** Intervals from {@code minChar} to {@code maxChar}, 20% of them with a single character. */
  public static Arbitrary<Interval> intervals(int minChar, int maxChar) {
    Arbitrary<Integer> chars = Arbitraries.integers().between(minChar, maxChar);
    return Arbitraries.frequencyOf(
        Tuple.of(
            4,
            Combinators.combine(chars, chars)
                .as((a, b) -> new Interval(Math.min(a, b), Math.max(a, b)))),
        Tuple.of(1, chars.map(Interval::ofCharacter)));
  }
}
