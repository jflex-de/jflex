package jflex.ucd_generator.scanner;

import static jflex.ucd_generator.util.HexaUtils.intFromHexa;

import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import jflex.ucd_generator.scanner.AutoValue_UnicodeData.Builder;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.CodepointRangeSet;

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

  public static Builder builder() {
    return new AutoValue_UnicodeData.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    private Map<String, List<CodepointRange.Builder>> mPropertyValueIntervals = new HashMap<>();

    abstract ImmutableSortedMap.Builder<String, CodepointRangeSet> propertyValueIntervalsBuilder();

    public abstract ImmutableMap.Builder<Integer, ImmutableSortedSet<Integer>>
        caselessMatchPartitionsBuilder();

    Map<Integer, SortedSet<Integer>> caselessMatchPartitions = new HashMap<>();

    /**
     * Grows the partition containing the given codePoint and its caseless equivalents, if any, to
     * include all of them.
     *
     * @param codePoint The code point to include in a caselessly equivalent partition
     * @param uppercaseMapping A hex String representation of the uppercase mapping of codePoint, or
     *     null if there isn't one
     * @param lowercaseMapping A hex String representation of the lowercase mapping of codePoint, or
     *     null if there isn't one
     * @param titlecaseMapping A hex String representation of the titlecase mapping of codePoint, or
     *     null if there isn't one
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
              .map(cp -> caselessMatchPartitions.get(cp))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(new TreeSet<>());

      // TODO
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
    Builder addPropertyInterval(String propName, int startCodePoint, int endCodePoint) {
      List<CodepointRange.Builder> values = mPropertyValueIntervals.get(propName);
      if (values == null) {
        values = new ArrayList<>();
        mPropertyValueIntervals.put(propName, values);
      }
      values.add(CodepointRange.builder(startCodePoint, endCodePoint));
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
      addAllPropertyValueIntervals();
      return internalBuild();
    }

    private void addAllPropertyValueIntervals() {
      for (String propName : mPropertyValueIntervals.keySet()) {
        CodepointRangeSet rangeSet =
            CodepointRangeSet.builder().addAll(mPropertyValueIntervals.get(propName)).build();
        propertyValueIntervalsBuilder().put(propName, rangeSet);
      }
    }
  }
}
