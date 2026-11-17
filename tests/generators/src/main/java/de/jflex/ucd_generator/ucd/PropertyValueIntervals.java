/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2019-2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.ucd;

import static de.jflex.ucd.SurrogateUtils.isSurrogateProperty;
import static de.jflex.ucd.SurrogateUtils.removeSurrogates;
import static de.jflex.ucd_generator.ucd.PropertyNames.NORMALIZED_GENERAL_CATEGORY;

import de.jflex.ucd.CodepointRange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PropertyValueIntervals {

  private static final boolean DEBUG = false;

  private final PropertyValues propertyValues;

  Set<String> usedBinaryProperties = new HashSet<>();

  /** Maps an enumerated property name to its used values. */
  private final Map<String, Set<String>> usedEnumProperties = new HashMap<>();

  /** Maps a property to its ranges; properties and ranges are both kept sorted. */
  private final SortedMap<String, SortedSet<CodepointRange>> propertyValueIntervals =
      new TreeMap<>();

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
    String canonicalValue = PropertyNames.canonicalValue(propName, propValue);
    boolean added = addPropertyInterval(canonicalValue, startCodePoint, endCodePoint);
    if (added) {
      usedEnumProperties.computeIfAbsent(propName, k -> new HashSet<>()).add(propValue);
    }
    return added;
  }

  private boolean addPropertyInterval(String propName, int startCodePoint, int endCodePoint) {
    if (isSurrogateProperty(propName)) {
      // Skip surrogate properties [U+D800-U+DFFF].
      // e.g. \p{Cs} - can't be represented in valid UTF-16 encoded strings.
      return false;
    }
    List<CodepointRange> ranges = removeSurrogates(startCodePoint, endCodePoint);
    if (ranges.isEmpty()) {
      return false;
    }
    boolean added = addRanges(propName, ranges);
    checkPropertyIntervalsState(propName, ranges);
    return added;
  }

  /**
   * Adds the ranges to the ranges of the property.
   *
   * @return whether a range was added that the property did not have yet.
   */
  private boolean addRanges(String propName, Collection<CodepointRange> ranges) {
    if (ranges.isEmpty()) {
      return false;
    }
    return propertyValueIntervals
        .computeIfAbsent(propName, k -> new TreeSet<>(CodepointRange.COMPARATOR))
        .addAll(ranges);
  }

  /** The ranges of the property, empty if it has none. */
  private SortedSet<CodepointRange> rangesOf(String propName) {
    SortedSet<CodepointRange> ranges = propertyValueIntervals.get(propName);
    return ranges != null ? ranges : new TreeSet<>(CodepointRange.COMPARATOR);
  }

  /** Assert property intervals are sorted. */
  private void checkPropertyIntervalsState(
      String propName, Collection<CodepointRange> addedRanges) {
    if (DEBUG && !isOrdered(rangesOf(propName))) {
      throw new IllegalStateException(
          String.format(
              "Property value intervals not order for %s after adding %s",
              propName,
              addedRanges.stream().map(CodepointRange::toString).collect(Collectors.joining(","))));
    }
  }

  private static boolean isOrdered(Collection<CodepointRange> ranges) {
    CodepointRange prev = null;
    for (CodepointRange range : ranges) {
      if (prev != null && CodepointRange.COMPARATOR.compare(prev, range) > 0) {
        return false;
      }
      prev = range;
    }
    return true;
  }

  public void addAllRanges(String propertyName, Collection<CodepointRange> ranges) {
    addRanges(propertyName, ranges);
    usedBinaryProperties.add(propertyName);
  }

  /**
   * @deprecated Hack for Unicode 2.0
   */
  @Deprecated
  public void removeEnumPropertyPoint(String propertyName, String propertyValue, int codepoint) {
    CodepointRange point = CodepointRange.createPoint(codepoint);
    String canonicalName =
        PropertyNames.canonicalValue(PropertyNames.normalize(propertyName), propertyValue);
    SortedSet<CodepointRange> ranges = rangesOf(canonicalName);
    CodepointRange range = ranges.headSet(point).last();
    ranges.remove(range);
    // In practice there is only one \ufe70\ufefe ; but in theory the removed point could have been
    // in the middle.
    List<CodepointRange> hackedRanges =
        CodepointRangeSet.builder().add(range).substract(point).build().ranges();
    ranges.addAll(hackedRanges);
  }

  List<CodepointRange> getRanges(String propName) {
    return Collections.unmodifiableList(new ArrayList<>(rangesOf(propName)));
  }

  /** Returns a copy of the used values of each enumerated property. */
  public Map<String, Set<String>> usedEnumeratedProperties() {
    Map<String, Set<String>> multimap = new LinkedHashMap<>();
    for (Map.Entry<String, Set<String>> entry : usedEnumProperties.entrySet()) {
      multimap.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
    }
    // First letter is added for General_category such as
    // gc ; C         ; Other                            # Cc | Cf | Cn | Co | Cs
    // gc ; Cc        ; Control                          ; cntrl
    // gc ; Cf        ; Format
    // etc.
    if (usedEnumProperties.containsKey(NORMALIZED_GENERAL_CATEGORY)) {
      for (String value : usedEnumProperties.get(NORMALIZED_GENERAL_CATEGORY)) {
        if (value.length() == 2) {
          multimap.get(NORMALIZED_GENERAL_CATEGORY).add(value.substring(0, 1));
        }
      }
    }
    return multimap;
  }

  public boolean hasUsedEnumeratedProperty(String category) {
    return usedEnumProperties.containsKey(category);
  }

  public Set<String> keySet() {
    return propertyValueIntervals.keySet();
  }

  public SortedMap<String, CodepointRangeSet> asSortedMap() {
    SortedMap<String, CodepointRangeSet> map = new TreeMap<>();
    for (Map.Entry<String, SortedSet<CodepointRange>> entry : propertyValueIntervals.entrySet()) {
      map.put(
          entry.getKey(), CodepointRangeSet.builder().addAllImmutable(entry.getValue()).build());
    }
    return map;
  }

  public boolean codePointInProperty(int codepoint, String propName) {
    // Ranges do not overlap, so the only candidate is the last range that starts at or before the
    // code point, i.e. the last range before the point following the code point.
    CodepointRange point = CodepointRange.createPoint(codepoint);
    SortedSet<CodepointRange> ranges = rangesOf(propName);
    SortedSet<CodepointRange> head = ranges.headSet(CodepointRange.createPoint(codepoint + 1));
    if (head.isEmpty()) {
      return false;
    }
    return head.last().contains(point);
  }
}
