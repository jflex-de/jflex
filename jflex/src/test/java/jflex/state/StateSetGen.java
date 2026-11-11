/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

/**
 * Generator for random {@link StateSet} instances.
 *
 * <p>{@link Size} annotation bounds the number of elements, {@link IntRange} the values.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see StateSet
 */
public class StateSetGen implements ArbitraryProvider {

  /** Default max bound for number of elements */
  private static final int MAX_SIZE = 10;

  /** Default max bound for size of elements */
  static final int MAX_RANGE = 1000;

  @Override
  public boolean canProvideFor(TypeUsage targetType) {
    return targetType.isOfType(StateSet.class);
  }

  @Override
  public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
    Optional<Size> size = targetType.findAnnotation(Size.class);
    Optional<IntRange> range = targetType.findAnnotation(IntRange.class);
    return Collections.singleton(
        stateSets(
            size.map(Size::min).orElse(0),
            size.map(Size::max).orElse(MAX_SIZE),
            range.map(IntRange::min).orElse(0),
            range.map(IntRange::max).orElse(MAX_RANGE)));
  }

  /**
   * StateSets with {@code minSize} to {@code maxSize} elements from {@code minRange} to {@code
   * maxRange}, and one large element 20% of the time.
   */
  static Arbitrary<StateSet> stateSets(int minSize, int maxSize, int minRange, int maxRange) {
    Arbitrary<List<Integer>> elements =
        Arbitraries.integers()
            .between(minRange, maxRange)
            .list()
            .ofMinSize(minSize)
            .ofMaxSize(maxSize);
    Arbitrary<Optional<Integer>> largeElement =
        Arbitraries.integers().between(minRange + 100_000, maxRange + 100_000).optional(0.2);
    return Combinators.combine(elements, largeElement)
        .as(
            (elems, large) -> {
              StateSet result = new StateSet();
              elems.forEach(result::addState);
              large.ifPresent(result::addState);
              return result;
            });
  }
}
