package jflex.ucd_generator.scanner.model;

import static jflex.ucd_generator.util.HexaUtils.intFromHexa;

import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import jflex.ucd_generator.ucd.Version;
import jflex.ucd_generator.util.PropertyNameNormalizer;

@AutoValue
public abstract class UnicodeData {

  /**
   * A set of code point space partitions, each containing at least two caselessly equivalent code
   * points.
   */
  public abstract ImmutableMap<Integer, ImmutableSortedSet<Integer>> caselessMatchPartitions();

  public abstract PropertyValues propertyValues();

  /** Maps Unicode property values to the associated set of code point ranges. */
  public abstract PropertyValueIntervals propertyValueIntervals();

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

    abstract Builder propertyValueIntervals(PropertyValueIntervals propertyValueIntervals);

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

    public abstract Builder maximumCodePoint(int codepoint);

    public abstract int maximumCodePoint();

    public abstract PropertyValues.Builder propertyValuesBuilder();
    public abstract Builder propertyValues(PropertyValues propertyValues);
    public abstract PropertyValueIntervals.Builder propertyValueIntervalsBuilder();


    abstract UnicodeData internalBuild();

    public UnicodeData build() {
      addInternalCaselessMatches();
      return internalBuild();
    }

    private void addInternalCaselessMatches() {
      for (Map.Entry<Integer, SortedSet<Integer>> entry : mCaselessMatchPartitions.entrySet()) {
        caselessMatchPartitionsBuilder()
            .put(entry.getKey(), ImmutableSortedSet.copyOfSorted(entry.getValue()));
      }
    }

    public String getCanonicalPropertyName(String propertyAlias) {
      return mPropertyNameNormalizer.getCanonicalPropertyName(propertyAlias);
    }

    public void addPropertyAlias(String alias, String normalizedLongName) {
      mPropertyNameNormalizer.putPropertyAlias(alias, normalizedLongName);
    }

    public String getCanonicalPropertyValueName(String propertyAlias) {
      return mPropertyNameNormalizer.getCanonicalPropertyValueName(propertyAlias);
    }

    public void addPropertyInterval(String propertyName, int start, int end) {
      propertyValueIntervalsBuilder().addPropertyInterval(propertyName, start, end,
          mPropertyNameNormalizer);
    }

    public void addPropertyInterval(String propName, String propValue, int start, int end) {
      propertyValueIntervalsBuilder().addPropertyInterval(propName, propValue, start, end,
          mPropertyNameNormalizer);
    }
  }
}
