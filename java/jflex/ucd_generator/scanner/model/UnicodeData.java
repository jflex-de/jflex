package jflex.ucd_generator.scanner.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.Version;
import jflex.ucd_generator.util.PropertyNameNormalizer;

public class UnicodeData {
  private final PropertyNameNormalizer propertyNameNormalizer = new PropertyNameNormalizer();

  private final PropertyValues propertyValues = new PropertyValues();

  /** Maps Unicode property values to the associated set of code point ranges. */
  private final PropertyValueIntervals propertyValueIntervals = new PropertyValueIntervals();

  private final CaselessMatches caselessMatches = new CaselessMatches();

  private int maximumCodePoint;

  private final Version version;

  public UnicodeData(Version version) {
    this.version = version;
  }

  public int maxCaselessMatchPartitionSize() {
    return caselessMatches.maxCaselessMatchPartitionSize();
  }

  public ImmutableCollection<SortedSet<Integer>> uniqueCaselessMatchPartitions() {
    return caselessMatches.uniqueCaselessMatchPartitions();
  }

  public void addCaselessMatches(
      int codePoint, String uppercaseMapping, String lowercaseMapping, String titlecaseMapping) {
    caselessMatches.addCaselessMatches(
        codePoint, uppercaseMapping, lowercaseMapping, titlecaseMapping);
  }

  public int maximumCodePoint() {
    return maximumCodePoint;
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
    propertyNameNormalizer.putPropertyAlias(alias, normalizedLongName);
  }

  public void addPropertyValueAliases(
      String propertyName, String normalizedPropertyValue, Set<String> aliases) {
    propertyValues.addPropertyValueAliases(propertyName, normalizedPropertyValue, aliases);
  }

  public Set<String> getPropertyAliases(String propName) {
    return propertyValues.getPropertyAliases(PropertyNameNormalizer.normalize(propName));
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

  public List<String> propertyValueIntervals() {
    ArrayList<String> list = new ArrayList<>(propertyValueIntervals.keySet());
    Collections.sort(list);
    return list;
  }

  public ImmutableList<CodepointRange> getPropertyValueIntervals(String propName) {
    return propertyValueIntervals.getRanges(propName);
  }

  public boolean hasUsedEnumeratedProperty(String category) {
    return usedEnumeratedProperties().containsKey(category);
  }
}
