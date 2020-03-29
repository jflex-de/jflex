package jflex.ucd_generator.scanner.model;

import static jflex.ucd_generator.util.SurrogateUtils.isSurrogate;
import static jflex.ucd_generator.util.SurrogateUtils.removeSurrogates;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.util.PropertyNameNormalizer;

public class PropertyValueIntervals {

  Set<String> usedBinaryProperties = new HashSet<>();

  Multimap<String, String> usedEnumProperties = HashMultimap.create();

  private final Multimap<String, CodepointRange> propertyValueIntervals = HashMultimap.create();

  @SuppressWarnings("unused") // TODO(regisd) This should be used after scanning.
  private void addCompatibilityProperties() {
    propertyValueIntervals.putAll("blank", createBlankSet());
  }

  private Collection<CodepointRange> createBlankSet() {
    if (propertyValueIntervals.containsKey("Zs")) {
      return propertyValueIntervals.get("Zs");
    } else {
      return propertyValueIntervals.get("whitespace");
    }
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
    propValue = propertyNameNormalizer.getCanonicalPropertyName(propValue);
    addBinaryPropertyInterval(
        propName + "=" + propValue, startCodePoint, endCodePoint, propertyNameNormalizer);
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

  public Map<String, Collection<CodepointRange>> asMap() {
    return Multimaps.asMap(propertyValueIntervals);
  }
}
