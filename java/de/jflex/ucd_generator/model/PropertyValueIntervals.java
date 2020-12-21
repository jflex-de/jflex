package de.jflex.ucd_generator.model;

import static de.jflex.ucd_generator.util.PropertyNameNormalizer.NORMALIZED_GENERAL_CATEGORY;
import static de.jflex.ucd_generator.util.SurrogateUtils.isSurrogate;
import static de.jflex.ucd_generator.util.SurrogateUtils.removeSurrogates;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class PropertyValueIntervals {

  private static final boolean DEBUG = false;

  private final PropertyValues propertyValues;

  Set<String> usedBinaryProperties = new HashSet<>();

  private final Multimap<String, String> usedEnumProperties = HashMultimap.create();

  // We need to keep the order of the added CodepointRanges
  private final SortedSetMultimap<String, CodepointRange> propertyValueIntervals =
      TreeMultimap.create(Ordering.natural(), CodepointRange.COMPARATOR);

  public PropertyValueIntervals(PropertyValues propertyValues) {
    this.propertyValues = propertyValues;
  }

  /**
   * Given a binary property name, and starting and ending code points, adds the interval to the
   * {@link #propertyValueIntervals} map.
   *
   * @param propName The property name, e.g. "Assigned".
   * @param startCodePoint The first code point in the interval.
   * @param endCodePoint The last code point in the interval.
   */
  boolean addBinaryPropertyInterval(String propName, int startCodePoint, int endCodePoint) {
    boolean added = addPropertyInterval(propName, startCodePoint, endCodePoint);
    if (added) {
      usedBinaryProperties.add(propName);
    }
    return added;
  }

  boolean addEnumPropertyInterval(
      String propName, String propValue, int startCodePoint, int endCodePoint) {
    propValue = propertyValues.getCanonicalValueName(propName, propValue);
    String canonicalValue = PropertyNameNormalizer.canonicalValue(propName, propValue);
    boolean added = addPropertyInterval(canonicalValue, startCodePoint, endCodePoint);
    if (added) {
      usedEnumProperties.put(propName, propValue);
    }
    return added;
  }

  private boolean addPropertyInterval(String propName, int startCodePoint, int endCodePoint) {
    if (isSurrogate(propName)) {
      // Skip surrogate properties [U+D800-U+DFFF].
      // e.g. \p{Cs} - can't be represented in valid UTF-16 encoded strings.
      return false;
    }
    List<CodepointRange> ranges = removeSurrogates(startCodePoint, endCodePoint);
    if (ranges.isEmpty()) {
      return false;
    }
    boolean added = propertyValueIntervals.putAll(propName, ranges);
    if (DEBUG) {
      try {
        Preconditions.checkState(
            Ordering.from(CodepointRange.COMPARATOR)
                .isOrdered(propertyValueIntervals.get(propName)));
      } catch (IllegalStateException e) {
        String strRanges =
            ranges.stream().map(CodepointRange::toString).collect(Collectors.joining(","));
        throw new IllegalStateException(
            String.format(
                "Property value intervals not order for %s after adding %s", propName, strRanges),
            e);
      }
    }
    return added;
  }

  public void addAllRanges(String propertyName, Collection<CodepointRange> ranges) {
    propertyValueIntervals.putAll(propertyName, ranges);
    usedBinaryProperties.add(propertyName);
  }

  ImmutableList<CodepointRange> getRanges(String propName) {
    Collection<CodepointRange> ranges = propertyValueIntervals.get(propName);
    if (ranges.isEmpty()) {
      return ImmutableList.of();
    }
    return ImmutableList.copyOf(ranges);
  }

  public ImmutableMultimap<String, String> usedEnumeratedProperties() {
    ImmutableSetMultimap.Builder<String, String> multimap = ImmutableSetMultimap.builder();
    multimap.putAll(usedEnumProperties);
    // First letter is added for General_category such as
    // gc ; C         ; Other                            # Cc | Cf | Cn | Co | Cs
    // gc ; Cc        ; Control                          ; cntrl
    // gc ; Cf        ; Format
    // etc.
    if (usedEnumProperties.containsKey(NORMALIZED_GENERAL_CATEGORY)) {
      for (String value : usedEnumProperties.get(NORMALIZED_GENERAL_CATEGORY)) {
        if (value.length() == 2) {
          multimap.put(NORMALIZED_GENERAL_CATEGORY, value.substring(0, 1));
        }
      }
    }
    return multimap.build();
  }

  public boolean hasUsedEnumeratedProperty(String category) {
    return usedEnumProperties.containsKey(category);
  }

  public Set<String> keySet() {
    return propertyValueIntervals.keySet();
  }

  public ImmutableSortedMap<String, CodepointRangeSet> asSortedMap() {
    ImmutableSortedMap.Builder<String, CodepointRangeSet> map = ImmutableSortedMap.naturalOrder();
    for (String property : propertyValueIntervals.keySet()) {
      map.put(
          property,
          CodepointRangeSet.builder()
              .addAllImmutable(propertyValueIntervals.get(property))
              .build());
    }
    return map.build();
  }

  public boolean codePointInProperty(int codepoint, String propName) {
    // The codepoint could be in the last range stating before
    CodepointRange point = CodepointRange.createPoint(codepoint);
    SortedSet<CodepointRange> ranges = propertyValueIntervals.get(propName);
    SortedSet<CodepointRange> head = ranges.headSet(point);
    if (head.isEmpty()) {
      return false;
    }
    return head.last().contains(point);
  }
}
