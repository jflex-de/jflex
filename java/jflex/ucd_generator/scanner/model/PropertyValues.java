package jflex.ucd_generator.scanner.model;

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
        propertyValueAlias2CanonicalValue.computeIfAbsent(propertyName, k -> new HashMap());
    for (String propertyValueAlias : aliases) {
      aliasMap.put(PropertyNameNormalizer.normalize(propertyValueAlias), normalizedPropertyValue);
    }
  }

  public Set<String> getPropertyAliases(String propName) {
    Multimap<String, String> aliases = allPropertyValueAliases.get(propName);
    if (aliases == null) {
      return ImmutableSet.of(propName);
    }
    return aliases.keySet();
  }

  public Collection<String> getPropertyValueAliases(String propName, String propValue) {
    Multimap<String, String> aliases = allPropertyValueAliases.get(propName);
    if (aliases == null) {
      return ImmutableSet.of(propValue);
    }
    return aliases.get(propValue);
  }
}
