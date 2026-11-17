/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodePointRanges {

  public static List<CodepointRange> merge(List<CodepointRange> ranges) {
    ranges = sort(ranges);
    List<CodepointRange> retval = new ArrayList<>();
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
    return Collections.unmodifiableList(retval);
  }

  public static List<CodepointRange> sort(List<CodepointRange> ranges) {
    List<CodepointRange> sorted = new ArrayList<>(ranges);
    sorted.sort(CodepointRange.COMPARATOR);
    return Collections.unmodifiableList(sorted);
  }

  private CodePointRanges() {}
}
