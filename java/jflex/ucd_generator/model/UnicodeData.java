package jflex.ucd_generator.model;

import static java.util.Arrays.asList;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.CodepointRangeSet;
import jflex.ucd_generator.ucd.MutableCodepointRange;
import jflex.ucd_generator.ucd.Version;
import jflex.ucd_generator.util.PropertyNameNormalizer;

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
      String propertyName, String normalizedPropertyValue, Set<String> aliases) {
    propertyValues.addPropertyValueAliases(propertyName, normalizedPropertyValue, aliases);
  }

  public Collection<String> getPropertyAliases(String propName) {
    return propertyNameNormalizer.getPropertyAliases(propName);
  }

  public Collection<String> getPropertyValueAliases(String propName, String propValue) {
    return propertyValues.getPropertyValueAliases(propName, propValue);
  }

  public void addPropertyInterval(String propertyName, int start, int end) {
    propertyValueIntervals.addBinaryPropertyInterval(
        propertyName, start, end, propertyNameNormalizer);
  }

  public void addPropertyInterval(String propName, String propValue, int start, int end) {
    propertyValueIntervals.addEnumPropertyInterval(
        propName, propValue, start, end, propertyNameNormalizer);
  }

  public Set<String> usedBinaryProperties() {
    return propertyValueIntervals.usedBinaryProperties;
  }

  public Multimap<String, String> usedEnumeratedProperties() {
    return propertyValueIntervals.usedEnumProperties;
  }

  public ImmutableList<CodepointRange> getPropertyValueIntervals(String propName) {
    return propertyValueIntervals.getRanges(
        propertyNameNormalizer.getCanonicalPropertyName(propName));
  }

  public boolean hasUsedEnumeratedProperty(String category) {
    return usedEnumeratedProperties().containsKey(category);
  }

  public int maximumCodePoint() {
    return maximumCodePoint;
  }

  public List<String> propertyValues() {
    return ImmutableList.sortedCopyOf(propertyValueIntervals.keySet());
  }

  /** Returns the code point range by property. */
  public ImmutableSortedMap<String, ImmutableCollection<CodepointRange>> intervals() {
    return propertyValueIntervals.asSortedMap();
  }

  public ImmutableList<String> propertyValueAliases() {
    // TODO(regisd) Implement this
    return ImmutableList.of(
        "ahex", "block=alphabeticpresentationforms", "block=ancientgreeknumbers", "unknown");
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
