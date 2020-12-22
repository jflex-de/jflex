package de.jflex.ucd_generator.ucd;

import static de.jflex.ucd_generator.util.PropertyNameNormalizer.NORMALIZED_GENERAL_CATEGORY;
import static de.jflex.ucd_generator.util.PropertyNameNormalizer.NORMALIZED_SCRIPT;
import static java.util.Arrays.asList;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSortedMap;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import de.jflex.version.Version;
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

  /** Returns the code point range by property. */
  public ImmutableSortedMap<String, CodepointRangeSet> intervals() {
    ImmutableSortedMap<String, CodepointRangeSet> map = propertyValueIntervals.asSortedMap();
    return ImmutableSortedMap.<String, CodepointRangeSet>naturalOrder()
        .putAll(map.entrySet())
        .build();
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
        Collection<String> propertyValueAliases = getPropertyValueAliases(propName, propValue);

        // Add value-only aliases for General Category and Script properties.
        if (Objects.equals(propName, NORMALIZED_SCRIPT)
            || Objects.equals(propName, NORMALIZED_GENERAL_CATEGORY)) {
          canonicalValue = propValue;
          for (String valueAlias : propertyValueAliases) {
            if (!Objects.equals(valueAlias, propValue)) {
              usedPropertyValueAliases.put(valueAlias, propValue);
            }
          }
        }
        for (String nameAlias : getPropertyAliases(propName)) {
          if (nameAlias.equals("blk") && version.equals(Versions.VERSION_3_2)) {
            // TODO(regisd) Can we remove this hack?
            // Ugly hack https://github.com/jflex-de/jflex/pull/828#issuecomment-749690037
            continue;
          }
          for (String valueAlias : propertyValueAliases) {
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
    ranges.substractAll(propertyValueIntervals.getRanges("zl")); // \p{gc=Line_Separator}
    ranges.substractAll(propertyValueIntervals.getRanges("zp")); // \p{gc=Paragraph_Separator}

    return ranges.build().ranges();
  }

  private ImmutableList<CodepointRange> createGraphSet() {
    CodepointRangeSet.Builder ranges = CodepointRangeSet.builder();
    ranges.add(MutableCodepointRange.create(0x0, maximumCodePoint));
    ranges.substractAll(propertyValueIntervals.getRanges("whitespace"));
    ranges.substractAll(propertyValueIntervals.getRanges("cc")); // \p{gc=Control}
    ranges.substractAll(propertyValueIntervals.getRanges("cn")); // \p{gc=Unassigned}
    ranges.substract(CodepointRange.create(0xD800, 0xDFFF));
    return ranges.build().ranges();
  }

  private ImmutableList<CodepointRange> createPrintSet() {
    CodepointRangeSet.Builder ranges = CodepointRangeSet.builder();
    ranges.addAllImmutable(propertyValueIntervals.getRanges("graph"));
    ranges.addAllImmutable(propertyValueIntervals.getRanges("blank"));
    ranges.substractAll(propertyValueIntervals.getRanges("cc")); // \p{gc=Control}
    return ranges.build().ranges();
  }

  public boolean codePointInProperty(int codepoint, String propName) {
    return propertyValueIntervals.codePointInProperty(codepoint, propName);
  }
}
