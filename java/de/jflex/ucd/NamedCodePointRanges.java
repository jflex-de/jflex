/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
