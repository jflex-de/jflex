/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import jflex.chars.Interval;
import jflex.chars.IntervalGen;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

/**
 * Generator for random {@link IntCharSet} instances.
 *
 * <p>{@link Size} annotation bounds the number of intervals, {@link IntRange} bounds the intervals.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see IntCharSet
 */
public class IntCharSetGen implements ArbitraryProvider {

  /** Default max bound for number of intervals */
  private static final int MAX_SIZE = 5;

  @Override
  public boolean canProvideFor(TypeUsage targetType) {
    return targetType.isOfType(IntCharSet.class);
  }

  @Override
  public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
    Optional<Size> size = targetType.findAnnotation(Size.class);
    return Collections.singleton(
        intCharSets(
            size.map(Size::min).orElse(0),
            size.map(Size::max).orElse(MAX_SIZE),
            IntervalGen.intervals(targetType.findAnnotation(IntRange.class))));
  }

  /** IntCharSets with the default bounds. */
  static Arbitrary<IntCharSet> intCharSets() {
    return intCharSets(0, MAX_SIZE, IntervalGen.intervals(Optional.empty()));
  }

  /**
   * IntCharSets of {@code minSize} to {@code maxSize} of the given intervals. Half of those below
   * {@code maxSize} get an additional cased character.
   */
  private static Arbitrary<IntCharSet> intCharSets(
      int minSize, int maxSize, Arbitrary<Interval> intervals) {
    Arbitrary<List<Interval>> intervalLists =
        intervals.list().ofMinSize(minSize).ofMaxSize(maxSize);
    return Combinators.combine(intervalLists, IntCharGen.casedChars().optional(0.5))
        .as(
            (list, cased) -> {
              IntCharSet result = new IntCharSet();
              list.forEach(result::add);
              if (list.size() < maxSize) {
                cased.ifPresent(result::add);
              }
              return result;
            });
  }
}
