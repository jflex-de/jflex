package de.jflex.ucd_generator.model;

import static de.jflex.ucd_generator.util.PropertyNameNormalizer.NORMALIZED_GENERAL_CATEGORY;
import static de.jflex.ucd_generator.util.PropertyNameNormalizer.NORMALIZED_SCRIPT;
import static java.util.Arrays.asList;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSortedMap;
import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.MutableCodepointRange;
import de.jflex.ucd_generator.ucd.Version;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

public class UnicodeData {
  private final PropertyNameNormalizer propertyNameNormalizer = new PropertyNameNormalizer();

  private final PropertyValues propertyValues = new PropertyValues();

  /** Maps Unicode property values to the associated set of code point ranges. */
  private final PropertyValueIntervals propertyValueIntervals =
      new PropertyValueIntervals(propertyValues);

  private final CaselessMatches caselessMatches = new CaselessMatches();

  private int maximumCodePoint;

  private final Version version;

  public UnicodeData(Version version) {
    this.version = version;
  }

  public void addCaselessMatches(
      int codePoint, String uppercaseMapping, String lowercaseMapping, String titlecaseMapping) {
    caselessMatches.addCaselessMatches(
        codePoint, uppercaseMapping, lowercaseMapping, titlecaseMapping);
  }

  public void maximumCodePoint(int maximumCodePoint) {
    this.maximumCodePoint = maximumCodePoint;
  }

  public Version version() {
    return version;
  }

  public String getCanonicalPropertyName(String propertyAlias) {
    return propertyNameNormalizer.getCanonicalPropertyName(propertyAlias);
  }

  public void addPropertyAlias(String alias, String normalizedLongName) {
    propertyNameNormalizer.putPropertyAlias(normalizedLongName, alias);
  }

  public void addPropertyValueAliases(
      String normalizedPropertyName, String normalizedPropertyValue, Set<String> aliases) {
    propertyValues.addPropertyValueAliases(
        normalizedPropertyName, normalizedPropertyValue, aliases);
  }

  public Collection<String> getPropertyValueAliases(String propName, String propValue) {
    return propertyValues.getPropertyValueAliases(propName, propValue);
  }

  public void copyPropertyValueAliases(String sourceProperty, String destProperty) {
    propertyValues.copyPropertyValueAliases(sourceProperty, destProperty);
    propertyNameNormalizer.putPropertyAlias(destProperty, destProperty);
  }

  public Collection<String> getPropertyAliases(String propName) {
    return propertyNameNormalizer.getPropertyAliases(propName);
  }

  public void addBinaryPropertyInterval(String propertyName, int start, int end) {
    propertyName = propertyNameNormalizer.getCanonicalPropertyName(propertyName);
    propertyValueIntervals.addBinaryPropertyInterval(propertyName, start, end);
  }

  public void addEnumPropertyInterval(String propName, String propValue, int start, int end) {
    propName = propertyNameNormalizer.getCanonicalPropertyName(propName);
    propertyValueIntervals.addEnumPropertyInterval(propName, propValue, start, end);
  }

  public Set<String> usedBinaryProperties() {
    return propertyValueIntervals.usedBinaryProperties;
  }

  public ImmutableMultimap<String, String> usedEnumeratedProperties() {
    return propertyValueIntervals.usedEnumeratedProperties();
  }

  public boolean hasUsedEnumeratedProperty(String category) {
    return propertyValueIntervals.hasUsedEnumeratedProperty(category);
  }

  public ImmutableList<CodepointRange> getPropertyValueIntervals(String propName) {
    return propertyValueIntervals.getRanges(
        propertyNameNormalizer.getCanonicalPropertyName(propName));
  }

  public int maximumCodePoint() {
    return maximumCodePoint;
  }

  public List<String> propertyValues() {
    return ImmutableList.copyOf(intervals().keySet());
  }

  /** Returns the code point range by property. */
  public ImmutableSortedMap<String, CodepointRangeSet> intervals() {
    ImmutableSortedMap<String, CodepointRangeSet> map = propertyValueIntervals.asSortedMap();
    // FIXME Why were script and casefolding emitted as short names?
    ImmutableSortedMap.Builder<String, CodepointRangeSet> retval =
        ImmutableSortedMap.naturalOrder();
    for (Map.Entry<String, CodepointRangeSet> e : map.entrySet()) {
      switch (e.getKey()) {
        case "casefolding":
          retval.put("cf", e.getValue());
          break;
        case "script":
          retval.put("sc", e.getValue());
          break;
        default:
          retval.put(e);
      }
    }
    return retval.build();
  }

  public ImmutableList<Map.Entry<String, String>> usedPropertyValueAliases() {
    return ImmutableList.<Map.Entry<String, String>>builder()
        .addAll(computeUsedPropertyValueAliases().entrySet())
        .build();
  }

  private ImmutableSortedMap<String, String> computeUsedPropertyValueAliases() {
    ImmutableSortedMap.Builder<String, String> usedPropertyValueAliases =
        ImmutableSortedMap.naturalOrder();
    for (String binaryProperty : usedBinaryProperties()) {
      for (String nameAlias : getPropertyAliases(binaryProperty)) {
        if (!Objects.equals(nameAlias, binaryProperty)) {
          usedPropertyValueAliases.put(nameAlias, binaryProperty);
        }
      }
    }
    ImmutableMultimap<String, String> usedEnumProperties =
        ImmutableMultimap.<String, String>builder()
            .putAll(usedEnumeratedProperties())
            .put(NORMALIZED_GENERAL_CATEGORY, "lc")
            .build();
    for (String propName : usedEnumProperties.keySet()) {
      for (String propValue : usedEnumProperties.get(propName)) {
        String canonicalValue = propName + '=' + propValue;

        // Add value-only aliases for General Category and Script properties.
        if (Objects.equals(propName, NORMALIZED_SCRIPT)
            || Objects.equals(propName, NORMALIZED_GENERAL_CATEGORY)) {
          canonicalValue = propValue;
          for (String valueAlias : getPropertyValueAliases(propName, propValue)) {
            if (!Objects.equals(valueAlias, propValue)) {
              usedPropertyValueAliases.put(valueAlias, propValue);
            }
          }
        }
        for (String nameAlias : getPropertyAliases(propName)) {
          for (String valueAlias : getPropertyValueAliases(propName, propValue)) {
            // Both property names and values have self-aliases; when generating
            // all possible alias combinations, exclude the one that is the same
            // as the full property name + full property value, unless the
            // property is General Category or Script.
            if (Objects.equals(propName, NORMALIZED_SCRIPT)
                || Objects.equals(propName, NORMALIZED_GENERAL_CATEGORY)
                || !(Objects.equals(nameAlias, propName)
                    && Objects.equals(valueAlias, propValue))) {
              String alias = nameAlias + '=' + valueAlias;
              usedPropertyValueAliases.put(alias, canonicalValue);
            }
          }
        }
      }
    }
    return usedPropertyValueAliases.build();
  }

  public String getCanonicalPropertyValueName(String propName, String propValue) {
    return propertyValues.getCanonicalValueName(propName, propValue);
  }

  public int maxCaselessMatchPartitionSize() {
    return caselessMatches.maxCaselessMatchPartitionSize();
  }

  public ImmutableCollection<SortedSet<Integer>> uniqueCaselessMatchPartitions() {
    return caselessMatches.uniqueCaselessMatchPartitions();
  }

  public void addCompatibilityProperties() {
    // TODO(regisd)

    // add xdigit
    // UTR#18: \p{xdigit} = [\p{gc=Decimal_Number}\p{Hex_Digit}]
    // \p{gc=Decimal_Number} = \p{Nd} (available in all versions)
    addCompatibilityProperty("xdigit", asList("nd", "hexdigit"));

    // add alnum
    // UTR#18: \p{alnum} = [\p{alpha}\p{digit}]
    // \p{alpha} = \p{Alphabetic} (available in all versions except 1.1)
    addCompatibilityProperty("alnum", asList("alphabetic", "nd"));

    // UTR#18: \p{blank} = [\p{Whitespace}
    //                      -- [\N{LF} \N{VT} \N{FF} \N{CR} \N{NEL}
    //                          \p{gc=Line_Separator} \p{gc=Paragraph_Separator}]]
    propertyValueIntervals.addAllRanges("blank", createBlankSet());

    // UTR#18: \p{graph} = [^\p{space}\p{gc=Control}\p{gc=Surrogate}\p{gc=Unassigned}]
    propertyValueIntervals.addAllRanges("graph", createGraphSet());

    // UTR#18: \p{print} = [\p{graph}\p{blank} -- \p{cntrl}]
    // \p{cntrl} = \p{gc=Control} = \p{gc=Cc} = \p{Cc}
    propertyValueIntervals.addAllRanges("print", createPrintSet());
  }

  private void addCompatibilityProperty(
      String newPropertyName, List<String> existingPropertyNames) {
    CodepointRangeSet.Builder ranges = CodepointRangeSet.builder();
    for (String p : existingPropertyNames) {
      ranges.addAllImmutable(propertyValueIntervals.getRanges(p));
    }
    propertyValueIntervals.addAllRanges(newPropertyName, ranges.build().ranges());
  }

  private ImmutableList<CodepointRange> createBlankSet() {
    CodepointRangeSet.Builder ranges = CodepointRangeSet.builder();
    ImmutableList<CodepointRange> whitespaceRanges = propertyValueIntervals.getRanges("whitespace");
    ranges.addAllImmutable(whitespaceRanges);
    // Subtract: [\N{LF}\N{VT}\N{FF}\N{CR}] = [U+000A-U+000D]
    ranges.substract(CodepointRange.create(0xA, 0xD));
    // Subtract: \N{NEL}
    ranges.substract(CodepointRange.create(0x85, 0x85));
    ranges.substract(propertyValueIntervals.getRanges("zl")); // \p{gc=Line_Separator}
    ranges.substract(propertyValueIntervals.getRanges("zp")); // \p{gc=Paragraph_Separator}

    return ranges.build().ranges();
  }

  private ImmutableList<CodepointRange> createGraphSet() {
    CodepointRangeSet.Builder ranges = CodepointRangeSet.builder();
    ranges.add(MutableCodepointRange.create(0x0, maximumCodePoint));
    ranges.substract(propertyValueIntervals.getRanges("whitespace"));
    ranges.substract(propertyValueIntervals.getRanges("cc")); // \p{gc=Control}
    ranges.substract(propertyValueIntervals.getRanges("cn")); // \p{gc=Unassigned}
    ranges.substract(CodepointRange.create(0xD800, 0xDFFF));
    return ranges.build().ranges();
  }

  private ImmutableList<CodepointRange> createPrintSet() {
    CodepointRangeSet.Builder ranges = CodepointRangeSet.builder();
    ranges.addAllImmutable(propertyValueIntervals.getRanges("graph"));
    ranges.addAllImmutable(propertyValueIntervals.getRanges("blank"));
    ranges.substract(propertyValueIntervals.getRanges("cc")); // \p{gc=Control}
    return ranges.build().ranges();
  }

  public boolean codePointInProperty(int codepoint, String propName) {
    return propertyValueIntervals.codePointInProperty(codepoint, propName);
  }
}
