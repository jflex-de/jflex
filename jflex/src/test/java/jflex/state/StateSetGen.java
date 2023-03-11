/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator for random {@link StateSet} instances.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 * @see StateSet
 */
public class StateSetGen extends Generator<StateSet> {

  /** Min bound for number of elements (0 = empty set) */
  private int minSize = 0;
  /** Max bound for number of elements */
  private int maxSize = 10;

  /** Min bound for size of elements (>= 0) */
  private int minRange = 0;
  /** Max bound for size of elements */
  private int maxRange = 1000;

  /** Constructs generator for StateSet instances */
  public StateSetGen() {
    super(StateSet.class);
  }

  @Override
  public StateSet generate(SourceOfRandomness r, GenerationStatus status) {
    StateSet result = new StateSet();

    int numElements = r.nextInt(minSize, maxSize);
    for (int i = 0; i < numElements; i++) {
      result.addState(r.nextInt(minRange, maxRange));
    }

    // add large value 20% of the time
    if (r.nextInt(1, 5) == 5) {
      result.addState(r.nextInt(minRange + 100_000, maxRange + 100_000));
    }

    return result;
  }

  /**
   * Configure this generator to only produce elements in the given range.
   *
   * @param range annotation that contains the elements constraints as minInt/maxInt
   */
  public void configure(InRange range) {
    minRange = Math.max(0, range.minInt());
    maxRange = range.maxInt();
  }

  /**
   * Configure this generator to only produce StateSets with a given range of number of elements.
   *
   * @param size annotation that contains how many elements the StateSet should contain at least and
   *     at most
   */
  public void configure(Size size) {
    minSize = Math.max(size.min(), 0);
    maxSize = size.max();
  }

  @Override
  public List<StateSet> doShrink(SourceOfRandomness random, StateSet larger) {
    List<StateSet> results = new ArrayList<>();

    if (!larger.containsElements()) {
      // cannot shrink further
      return results;
    }

    // Try smallest possible value = empty set
    results.add(new StateSet());

    // Try edge case set with single element 0
    results.add(new StateSet(1, 0));

    // Try successively from the smallest element
    StateSet nextSet = new StateSet();
    for (int elem : larger) {
      nextSet.addState(elem);
      results.add(nextSet);
      nextSet = new StateSet(nextSet);
    }

    return results;
  }
}
