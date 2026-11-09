/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Operations on lists of {@link Range}s. */
final class Ranges {
  static final int MAX_CODE_POINT = 0x10FFFF;

  /**
   * The last code point in {@code All.Unicode.BMP.characters.input}, the input of the cases for
   * Unicode versions before 3.0.
   */
  static final int MAX_BMP_CODE_POINT = 0xFFFD;

  private static final int FIRST_SURROGATE = 0xD800;
  private static final int LAST_SURROGATE = 0xDFFF;

  private static final Comparator<Range> BY_START =
      new Comparator<Range>() {
        @Override
        public int compare(Range a, Range b) {
          return Integer.compare(a.start, b.start);
        }
      };

  /** Sorts by start; ranges with the same start keep their order. */
  static List<Range> sortByStart(List<Range> ranges) {
    List<Range> sorted = new ArrayList<>(ranges);
    Collections.sort(sorted, BY_START);
    return sorted;
  }

  /**
   * Merges adjacent or overlapping ranges that have the same value.
   *
   * @param sorted ranges sorted by start
   */
  static List<Range> merge(List<Range> sorted) {
    List<Range> merged = new ArrayList<>();
    for (Range range : sorted) {
      Range last = merged.isEmpty() ? null : merged.get(merged.size() - 1);
      if (last != null && range.start <= last.end + 1 && sameValue(last, range)) {
        if (range.end > last.end) {
          merged.set(merged.size() - 1, new Range(last.start, range.end, last.value));
        }
      } else {
        merged.add(range);
      }
    }
    return merged;
  }

  private static boolean sameValue(Range a, Range b) {
    return a.value == null ? b.value == null : a.value.equals(b.value);
  }

  /**
   * The code points from 0 to {@code max} that are in none of the ranges, without the surrogates,
   * as ranges with {@code value}.
   *
   * @param sorted ranges sorted by start
   */
  static List<Range> gaps(List<Range> sorted, int max, String value) {
    List<Range> gaps = new ArrayList<>();
    int next = 0;
    for (Range range : sorted) {
      if (range.start > next) {
        gaps.add(new Range(next, range.start - 1, value));
      }
      next = Math.max(next, range.end + 1);
    }
    if (next <= max) {
      gaps.add(new Range(next, max, value));
    }
    return withoutSurrogates(gaps);
  }

  /** Removes the surrogate code points: a range that contains them is split or shortened. */
  static List<Range> withoutSurrogates(List<Range> ranges) {
    List<Range> result = new ArrayList<>();
    for (Range range : ranges) {
      if (range.end < FIRST_SURROGATE || range.start > LAST_SURROGATE) {
        result.add(range);
      } else {
        if (range.start < FIRST_SURROGATE) {
          result.add(new Range(range.start, FIRST_SURROGATE - 1, range.value));
        }
        if (range.end > LAST_SURROGATE) {
          result.add(new Range(LAST_SURROGATE + 1, range.end, range.value));
        }
      }
    }
    return result;
  }

  /** The same ranges with {@code value} as their value. */
  static List<Range> withValue(List<Range> ranges, String value) {
    List<Range> result = new ArrayList<>();
    for (Range range : ranges) {
      result.add(range.withValue(value));
    }
    return result;
  }

  /** Drops the ranges that start after {@code max}, and ends the others at {@code max} at most. */
  static List<Range> upTo(List<Range> ranges, int max) {
    List<Range> result = new ArrayList<>();
    for (Range range : ranges) {
      if (range.start <= max) {
        result.add(range.end <= max ? range : new Range(range.start, max, range.value));
      }
    }
    return result;
  }

  /**
   * The ranges of an enumerated property for all code points up to {@code max} except the
   * surrogates: the given ranges merged, and the gaps between them filled with {@code
   * defaultValue}.
   */
  static List<Range> complete(List<Range> ranges, String defaultValue, int max) {
    List<Range> merged = merge(sortByStart(ranges));
    List<Range> all = new ArrayList<>(merged);
    all.addAll(gaps(merged, max, defaultValue));
    return merge(sortByStart(all));
  }

  private Ranges() {}
}
