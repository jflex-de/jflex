package jflex.ucd_generator.model;

import static jflex.ucd_generator.util.SurrogateUtils.isSurrogate;
import static jflex.ucd_generator.util.SurrogateUtils.removeSurrogates;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingSortedSetMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.util.PropertyNameNormalizer;

public class PropertyValueIntervals {

  private static final boolean DEBUG = false;

  private final PropertyValues propertyValues;

  Set<String> usedBinaryProperties = new HashSet<>();

  Multimap<String, String> usedEnumProperties = HashMultimap.create();

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
  void addBinaryPropertyInterval(
      String propName,
      int startCodePoint,
      int endCodePoint,
      PropertyNameNormalizer propertyNameNormalizer) {
    propName = propertyNameNormalizer.getCanonicalPropertyName(propName);
    addPropertyInterval(propName, startCodePoint, endCodePoint);
    usedBinaryProperties.add(propName);
  }

  void addEnumPropertyInterval(
      String propName,
      String propValue,
      int startCodePoint,
      int endCodePoint,
      PropertyNameNormalizer propertyNameNormalizer) {
    propName = propertyNameNormalizer.getCanonicalPropertyName(propName);
    propValue = propertyValues.getCanonicalValueName(propName, propValue);
    addBinaryPropertyInterval(
        PropertyNameNormalizer.canonicalValue(propName, propValue),
        startCodePoint,
        endCodePoint,
        propertyNameNormalizer);
    usedEnumProperties.put(propName, propValue);
  }

  void addPropertyInterval(String propName, int startCodePoint, int endCodePoint) {
    if (isSurrogate(propName)) {
      // Skip surrogates
      return;
    }
    List<CodepointRange> ranges = removeSurrogates(startCodePoint, endCodePoint);
    if (ranges.isEmpty()) {
      return;
    }
    propertyValueIntervals.putAll(propName, ranges);
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

  public Set<String> keySet() {
    return propertyValueIntervals.keySet();
  }

  public ImmutableSortedMap<String, ImmutableCollection<CodepointRange>> asSortedMap() {
    ImmutableSortedMap.Builder<String, ImmutableCollection<CodepointRange>> map =
        ImmutableSortedMap.naturalOrder();
    for (String property : propertyValueIntervals.keySet()) {
      map.put(property, ImmutableList.copyOf(propertyValueIntervals.get(property)));
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

  static class PropertyValueMultiMap extends ForwardingSortedSetMultimap<String, CodepointRange> {

    @Override
    protected SortedSetMultimap<String, CodepointRange> delegate() {
      return null;
    }
  }
}
