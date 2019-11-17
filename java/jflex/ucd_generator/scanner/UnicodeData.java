package jflex.ucd_generator.scanner;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public static Builder builder() {
    return new AutoValue_UnicodeData.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    private Map<String, List<MutableCodepointRange>> mPropertyValueIntervals = new HashMap<>();

    abstract ImmutableMap.Builder<Integer, ImmutableSortedSet<Integer>>
        caselessMatchPartitionsBuilder();

    abstract ImmutableSortedMap.Builder<String, CodepointRangeSet> propertyValueIntervalsBuilder();

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
    Builder addCaselessMatches(
        int codePoint, String uppercaseMapping, String lowercaseMapping, String titlecaseMapping) {
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
