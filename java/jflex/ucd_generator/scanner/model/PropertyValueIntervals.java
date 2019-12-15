package jflex.ucd_generator.scanner.model;

import static jflex.ucd_generator.util.SurrogateUtils.isSurrogate;
import static jflex.ucd_generator.util.SurrogateUtils.removeSurrogates;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.MutableCodepointRange;
import jflex.ucd_generator.util.PropertyNameNormalizer;

public class PropertyValueIntervals {
  Set<String> usedBinaryProperties = new HashSet<>();

  Multimap<String, String> usedEnumProperties = HashMultimap.create();

  private final Map<String, List<MutableCodepointRange>> propertyValueIntervals = new HashMap<>();

  private void addCompatibilityProperties() {
    propertyValueIntervals.put("blank", createBlankSet());
  }

  private List<MutableCodepointRange> createBlankSet() {
    return Optional.ofNullable(propertyValueIntervals.get("Zs"))
        .orElse(propertyValueIntervals.get("whitespace"));
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
    addPropertyInterval(propName, startCodePoint, endCodePoint, propertyNameNormalizer);
    usedBinaryProperties.add(propName);
  }

  void addEnumPropertyInterval(
      String propName,
      String propValue,
      int startCodePoint,
      int endCodePoint,
      PropertyNameNormalizer propertyNameNormalizer) {
    addBinaryPropertyInterval(
        propName + "=" + propValue, startCodePoint, endCodePoint, propertyNameNormalizer);
    usedEnumProperties.put(propName, propValue);
  }

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
    List<MutableCodepointRange> intervals =
        propertyValueIntervals.computeIfAbsent(propName, k -> new ArrayList<>());
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
  }

  public ImmutableList<CodepointRange> getRanges(String propName) {
    List<MutableCodepointRange> ranges = propertyValueIntervals.get(propName);
    if (ranges == null) {
      return ImmutableList.of();
    }
    return ranges.stream().map(CodepointRange::create).collect(ImmutableList.toImmutableList());
  }

  public Set<String> keySet() {
    return propertyValueIntervals.keySet();
  }
}
