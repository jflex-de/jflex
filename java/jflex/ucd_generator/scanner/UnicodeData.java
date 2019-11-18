package jflex.ucd_generator.scanner;

import static jflex.ucd_generator.util.HexaUtils.intFromHexa;

import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
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
import java.util.stream.Stream;
import jflex.ucd_generator.ucd.CodepointRangeSet;
import jflex.ucd_generator.ucd.MutableCodepointRange;

@AutoValue
public abstract class UnicodeData {

  /**
   * A set of code point space partitions, each containing at least two caselessly equivalent code
   * points.
   */
  public abstract ImmutableMap<Integer, ImmutableSortedSet<Integer>> caselessMatchPartitions();

  /** Maps Unicode property values to the associated set of code point ranges. */
  public abstract ImmutableSortedMap<String, CodepointRangeSet> propertyValueIntervals();

  public abstract int maximumCodePoint();

  public int maxCaselessMatchPartitionSize() {
    return caselessMatchPartitions().values().stream()
        .map(Set::size)
        .max(Integer::compareTo)
        .orElseGet(() -> 0);
  }

  /**
   * Returns the {@link #caselessMatchPartitions()} where the key is the first element from the
   * partition.
   */
  public ImmutableCollection<SortedSet<Integer>> uniqueCaselessMatchPartitions() {
    ArrayList<SortedSet<Integer>> partitions = new ArrayList<>();
    for (Map.Entry<Integer, ImmutableSortedSet<Integer>> entry :
        caselessMatchPartitions().entrySet()) {
      if (entry.getKey().equals(entry.getValue().first())) {
        partitions.add(entry.getValue());
      }
    }
    Comparator<SortedSet<Integer>> comparator =
        (left, right) -> Integer.compare(left.first(), right.first());
    return ImmutableList.sortedCopyOf(comparator, partitions);
  }

  public static Builder builder() {
    return new AutoValue_UnicodeData.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    private Map<Integer, SortedSet<Integer>> mCaselessMatchPartitions = new HashMap<>();
    private Map<String, List<MutableCodepointRange>> mPropertyValueIntervals = new HashMap<>();

    abstract ImmutableSortedMap.Builder<String, CodepointRangeSet> propertyValueIntervalsBuilder();

    abstract ImmutableMap.Builder<Integer, ImmutableSortedSet<Integer>>
        caselessMatchPartitionsBuilder();

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
    public Builder addCaselessMatches(
        int codePoint, String uppercaseMapping, String lowercaseMapping, String titlecaseMapping) {
      if (Strings.isNullOrEmpty(uppercaseMapping)
          && Strings.isNullOrEmpty(lowercaseMapping)
          && Strings.isNullOrEmpty(titlecaseMapping)) {
        return this;
      }

      Integer upper = intFromHexa(uppercaseMapping);
      Integer lower = intFromHexa(lowercaseMapping);
      Integer title = intFromHexa(titlecaseMapping);
      SortedSet<Integer> partition =
          Stream.of(codePoint, upper, lower, title)
              .map(cp -> mCaselessMatchPartitions.get(cp))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(new TreeSet<>());
      for (Integer cp : Arrays.asList(codePoint, upper, lower, title)) {
        if (cp != null) {
          partition.add(cp);
          mCaselessMatchPartitions.put(cp, partition);
        }
      }
      return this;
    }

    /**
     * Given a binary property name, and starting and ending code points, adds the interval to the
     * {@link #propertyValueIntervals} map.
     *
     * @param propName The property name, e.g. "Assigned".
     * @param startCodePoint The first code point in the interval.
     * @param endCodePoint The last code point in the interval.
     */
    public Builder addPropertyInterval(String propName, int startCodePoint, int endCodePoint) {
      List<MutableCodepointRange> values = mPropertyValueIntervals.get(propName);
      if (values == null) {
        values = new ArrayList<>();
        mPropertyValueIntervals.put(propName, values);
      }
      values.add(new MutableCodepointRange(startCodePoint, endCodePoint));
      return this;
    }

    Builder addPropertyInterval(
        String propName, String propValue, int startCodePoint, int endCodePoint) {
      return addPropertyInterval(propName + "=" + propName, startCodePoint, endCodePoint);
    }

    public abstract Builder maximumCodePoint(int codepoint);

    public abstract int maximumCodePoint();

    abstract UnicodeData internalBuild();

    public UnicodeData build() {
      addInternalCaselessMatches();
      addInternalPropertyValueIntervals();
      return internalBuild();
    }

    private void addInternalCaselessMatches() {
      for (Map.Entry<Integer, SortedSet<Integer>> entry : mCaselessMatchPartitions.entrySet()) {
        caselessMatchPartitionsBuilder()
            .put(entry.getKey(), ImmutableSortedSet.copyOfSorted(entry.getValue()));
      }
    }

    private void addInternalPropertyValueIntervals() {
      for (String propName : mPropertyValueIntervals.keySet()) {
        CodepointRangeSet rangeSet =
            CodepointRangeSet.builder().addAll(mPropertyValueIntervals.get(propName)).build();
        propertyValueIntervalsBuilder().put(propName, rangeSet);
      }
    }
  }
}
