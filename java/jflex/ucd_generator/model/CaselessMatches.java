package jflex.ucd_generator.model;

import static jflex.ucd_generator.util.HexaUtils.intFromHexa;

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
        .orElseGet(() -> 0);
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
    Comparator<SortedSet<Integer>> comparator =
        (left, right) -> Integer.compare(left.first(), right.first());
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
            .map(cp -> caselessMatchPartitions.get(cp))
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
