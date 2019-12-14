package jflex.ucd_generator.scanner;

import static jflex.ucd_generator.scanner.SurrogateUtils.isSurrogate;
import static jflex.ucd_generator.scanner.SurrogateUtils.removeSurrogates;
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
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.CodepointRangeSet;
import jflex.ucd_generator.ucd.MutableCodepointRange;
import jflex.ucd_generator.ucd.Version;

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

  protected abstract Version version();

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

  public static Builder builder(Version ucdVersion) {
    return new AutoValue_UnicodeData.Builder().version(ucdVersion);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    private final PropertyNameNormalizer mPropertyNameNormalizer = new PropertyNameNormalizer();
    private final Map<Integer, SortedSet<Integer>> mCaselessMatchPartitions = new HashMap<>();
    private final Map<String, List<MutableCodepointRange>> mPropertyValueIntervals =
        new HashMap<>();

    abstract ImmutableSortedMap.Builder<String, CodepointRangeSet> propertyValueIntervalsBuilder();

    abstract ImmutableMap.Builder<Integer, ImmutableSortedSet<Integer>>
        caselessMatchPartitionsBuilder();

    abstract Builder version(Version version);

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

      List<Integer> codepoints =
          Arrays.asList(
              codePoint,
              intFromHexa(uppercaseMapping),
              intFromHexa(lowercaseMapping),
              intFromHexa(titlecaseMapping));
      SortedSet<Integer> partition =
          codepoints.stream()
              .filter(Objects::nonNull)
              .map(cp -> mCaselessMatchPartitions.get(cp))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(new TreeSet<>());
      for (Integer cp : codepoints) {
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
      propName = mPropertyNameNormalizer.getCanonicalPropertyName(propName);
      if (isSurrogate(propName)) {
        // Skip surrogates
        return this;
      }
      List<CodepointRange> ranges = removeSurrogates(startCodePoint, endCodePoint);
      if (ranges.isEmpty()) {
        return this;
      }
      List<MutableCodepointRange> intervals = getOrCreateIntervals(propName);
      for (CodepointRange range : ranges) {
        // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
        // range (starting point is listed as U+4E00).  This is U+9FFF according
        // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
        //
        //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
        //
        // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
        // value, as are the previous code points, so to include
        // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
        // TODO
        //        if (range.end() == 0x4E00 && Objects.equalsmajorMinor, "1.1")) {
        //          range.end = 0x9FFF;
        //        }
        intervals.add(new MutableCodepointRange(startCodePoint, endCodePoint));
      }
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
      addCompatibilityProperties();
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

    private void addCompatibilityProperties() {
      propertyValueIntervalsBuilder().put("blank", createBlankSet());
    }

    private CodepointRangeSet createBlankSet() {
      return CodepointRangeSet.builder()
          .addAll(getIntervals("Zs").orElse(mPropertyValueIntervals.get("whitespace")))
          .build();
    }

    private Optional<List<MutableCodepointRange>> getIntervals(String propName) {
      return Optional.ofNullable(mPropertyValueIntervals.get(propName));
    }

    private List<MutableCodepointRange> getOrCreateIntervals(String propName) {
      return mPropertyValueIntervals.computeIfAbsent(propName, k -> new ArrayList<>());
    }

    public void addPropertyAlias(String alias, String normalizedLongName) {
      mPropertyNameNormalizer.putPropertyAlias(alias, normalizedLongName);
    }
  }
}
