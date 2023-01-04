/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class CodePointRanges {

  public static ImmutableList<CodepointRange> merge(List<CodepointRange> ranges) {
    ranges = sort(ranges);
    ImmutableList.Builder<CodepointRange> retval = ImmutableList.builder();
    CodepointRange prev = ranges.get(0);
    for (int i = 1; i < ranges.size(); i++) {
      CodepointRange block = ranges.get(i);
      if (prev.end() + 1 == ranges.get(i).start()) {
        // merge the two blocks
        prev = CodepointRange.create(prev.start(), block.end());
      } else {
        retval.add(prev);
        prev = block;
      }
    }
    // add last
    retval.add(prev);
    return retval.build();
  }

  public static ImmutableList<CodepointRange> sort(List<CodepointRange> ranges) {
    return ImmutableList.sortedCopyOf(CodepointRange.COMPARATOR, ranges);
  }

  private CodePointRanges() {}
}
