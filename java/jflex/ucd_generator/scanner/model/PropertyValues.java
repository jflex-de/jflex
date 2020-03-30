package jflex.ucd_generator.scanner.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jflex.ucd_generator.util.PropertyNameNormalizer;

public class PropertyValues {

  /**
   * Property name: {property value: aliases of property value}
   * e.g.
   * <pre>{@code
   * key=age
   * value={
   *   unassigned=[na,unassigned]
   *   v90=[v90,9.0]
   *   etc.
   * }</pre>
   */
  private final Map<String, Multimap<String, String>> allPropertyValueAliases = new HashMap<>();

  /** Maps property value aliases to their corresponding canonical property values. */
  private final Map<String, Map<String, String>> propertyValueAlias2CanonicalValue =
      new HashMap<>();

  void addPropertyValueAliases(
      String propertyName, String normalizedPropertyValue, Set<String> aliases) {
    Multimap<String, String> aliasesForName =
        allPropertyValueAliases.computeIfAbsent(propertyName, k -> HashMultimap.create());
    aliasesForName.putAll(normalizedPropertyValue, aliases);

    Map<String, String> aliasMap =
        propertyValueAlias2CanonicalValue.computeIfAbsent(propertyName, k -> new HashMap<>());
    for (String propertyValueAlias : aliases) {
      aliasMap.put(PropertyNameNormalizer.normalize(propertyValueAlias), normalizedPropertyValue);
    }
  }

  public Collection<String> getPropertyValueAliases(String propName, String propValue) {
    Multimap<String, String> aliases = allPropertyValueAliases.get(propName);
    if (aliases == null) {
      return ImmutableSet.of(propValue);
    }
    return aliases.get(getCanonicalValueName(propName, propValue));
  }

  public String getCanonicalValueName(String normalizedPropName, String propValue) {
    Map<String, String> canonicalPropValueNames =
        Preconditions.checkNotNull(
            propertyValueAlias2CanonicalValue.get(normalizedPropName),
            "Unknown canonical name for %s",
            normalizedPropName);
    return canonicalPropValueNames.get(PropertyNameNormalizer.normalize(propValue));
  }
}
