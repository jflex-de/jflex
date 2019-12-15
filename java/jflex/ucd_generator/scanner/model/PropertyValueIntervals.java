package jflex.ucd_generator.scanner.model;

import static jflex.ucd_generator.util.SurrogateUtils.isSurrogate;
import static jflex.ucd_generator.util.SurrogateUtils.removeSurrogates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.MutableCodepointRange;
import jflex.ucd_generator.util.PropertyNameNormalizer;

public class PropertyValueIntervals implements Iterable<String> {
  Set<String> usedBinaryProperties = new HashSet<>();

  Map<String, Set<String>> usedEnumProperties = new HashMap<>();

  private final Map<String, List<MutableCodepointRange>> propertyValueIntervals = new HashMap<>();

  @Override
  public Iterator<String> iterator() {
    return propertyValueIntervals.keySet().iterator();
  }

  private void addCompatibilityProperties() {
    propertyValueIntervals.put("blank", createBlankSet());
  }

  private List<MutableCodepointRange> createBlankSet() {
    return getIntervals("Zs").orElse(propertyValueIntervals.get("whitespace"));
  }

  private List<MutableCodepointRange> getOrCreateIntervals(String propName) {
    return propertyValueIntervals.computeIfAbsent(propName, k -> new ArrayList<>());
  }

  private Optional<List<MutableCodepointRange>> getIntervals(String propName) {
    return Optional.ofNullable(propertyValueIntervals.get(propName));
  }

  /**
   * Given a binary property name, and starting and ending code points, adds the interval to the
   * {@link #propertyValueIntervals} map.
   *
   * @param propName The property name, e.g. "Assigned".
   * @param startCodePoint The first code point in the interval.
   * @param endCodePoint The last code point in the interval.
   */
  void addPropertyInterval(
      String propName,
      int startCodePoint,
      int endCodePoint,
      PropertyNameNormalizer propertyNameNormalizer) {
    propName = propertyNameNormalizer.getCanonicalPropertyName(propName);
    if (isSurrogate(propName)) {
      // Skip surrogates
      return;
    }
    List<CodepointRange> ranges = removeSurrogates(startCodePoint, endCodePoint);
    if (ranges.isEmpty()) {
      return;
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
      intervals.add(MutableCodepointRange.create(range));
    }
    usedBinaryProperties.add(propName);
  }

  void addPropertyInterval(
      String propName,
      String propValue,
      int startCodePoint,
      int endCodePoint,
      PropertyNameNormalizer propertyNameNormalizer) {
    addPropertyInterval(
        propName + "=" + propName, startCodePoint, endCodePoint, propertyNameNormalizer);
  }

  public List<MutableCodepointRange> getRanges(String propName) {
    return propertyValueIntervals.get(propName);
  }
}
