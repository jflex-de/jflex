/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2019-2020 Google, LLC.
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
package de.jflex.ucd_generator.ucd;

import static de.jflex.ucd_generator.util.HexaUtils.intFromHexa;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CaselessMatches {
  /**
   * A set of code point space partitions, each containing at least two caselessly equivalent code
   * points.
   */
  public final Map<Integer, SortedSet<Integer>> caselessMatchPartitions = new HashMap<>();

  public int maxCaselessMatchPartitionSize() {
    return caselessMatchPartitions.values().stream()
        .map(Set::size)
        .max(Integer::compareTo)
        .orElse(0);
  }

  /**
   * Returns the {@link #caselessMatchPartitions} where the key is the first element from the
   * partition.
   */
  public ImmutableCollection<SortedSet<Integer>> uniqueCaselessMatchPartitions() {
    ArrayList<SortedSet<Integer>> partitions = new ArrayList<>();
    for (Map.Entry<Integer, SortedSet<Integer>> entry : caselessMatchPartitions.entrySet()) {
      if (entry.getKey().equals(entry.getValue().first())) {
        partitions.add(entry.getValue());
      }
    }
    Comparator<SortedSet<Integer>> comparator = Comparator.comparingInt(SortedSet::first);
    return ImmutableList.sortedCopyOf(comparator, partitions);
  }

  /**
   * Grows the partition containing the given codePoint and its caseless equivalents, if any, to
   * include all of them.
   *
   * @param codePoint The code point to include in a caselessly equivalent partition
   * @param uppercaseMapping A hex String representation of the uppercase mapping of codePoint, or
   *     {@code null} if there isn't one
   * @param lowercaseMapping A hex String representation of the lowercase mapping of codePoint, or
   *     {@code null} if there isn't one
   * @param titlecaseMapping A hex String representation of the titlecase mapping of codePoint, or
   *     {@code null} if there isn't one
   */
  public void addCaselessMatches(
      int codePoint, String uppercaseMapping, String lowercaseMapping, String titlecaseMapping) {
    if (Strings.isNullOrEmpty(uppercaseMapping)
        && Strings.isNullOrEmpty(lowercaseMapping)
        && Strings.isNullOrEmpty(titlecaseMapping)) {
      return;
    }

    List<Integer> codepoints =
        Arrays.asList(
            codePoint,
            intFromHexa(uppercaseMapping),
            intFromHexa(lowercaseMapping),
            intFromHexa(titlecaseMapping));
    SortedSet<Integer> partition =
        codepoints.stream()
            .filter(Objects::nonNull)
            .map(caselessMatchPartitions::get)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(new TreeSet<>());
    for (Integer cp : codepoints) {
      if (cp != null) {
        partition.add(cp);
        caselessMatchPartitions.put(cp, partition);
      }
    }
  }
}
