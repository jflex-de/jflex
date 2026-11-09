/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;

public class NamedCodePointRanges {

  public static ImmutableList<NamedCodepointRange<String>> merge(
      List<NamedCodepointRange<String>> ranges) {
    ranges = sort(ranges);
    ImmutableList.Builder<NamedCodepointRange<String>> retval = ImmutableList.builder();
    NamedCodepointRange<String> prev = ranges.get(0);
    for (int i = 1; i < ranges.size(); i++) {
      NamedCodepointRange<String> block = ranges.get(i);
      if (prev.name().equals(block.name())
          && prev.range().end() + 1 == ranges.get(i).range().start()) {
        // merge the two blocks
        prev = NamedCodepointRange.create(block.name(), prev.range().start(), block.range().end());
      } else {
        retval.add(prev);
        prev = block;
      }
    }
    // add last
    retval.add(prev);
    return retval.build();
  }

  public static ImmutableList<NamedCodepointRange<String>> sort(
      List<NamedCodepointRange<String>> ranges) {
    Comparator<NamedCodepointRange<String>> comparator =
        (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());
    return ImmutableList.sortedCopyOf(comparator, ranges);
  }

  private NamedCodePointRanges() {}
}
