package jflex.ucd_generator.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import jdk.internal.jline.internal.Nullable;
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

  /**
   * Maps property value aliases to their corresponding canonical property values, for each
   * canonical name.
   */
  private final Map<String, Map<String, String>> propertyValueAlias2CanonicalValue =
      new HashMap<>();

  void addPropertyValueAliases(
      String propertyName, String normalizedPropertyValue, Set<String> aliases) {
    Multimap<String, String> aliasesForName =
        allPropertyValueAliases.computeIfAbsent(propertyName, k -> HashMultimap.create());
    aliasesForName.put(normalizedPropertyValue, normalizedPropertyValue);
    aliasesForName.putAll(normalizedPropertyValue, aliases);

    Map<String, String> aliasMap =
        propertyValueAlias2CanonicalValue.computeIfAbsent(propertyName, k -> new HashMap<>());
    for (String propertyValueAlias : aliases) {
      aliasMap.put(PropertyNameNormalizer.normalize(propertyValueAlias), normalizedPropertyValue);
    }
  }

  /**
   * Returns all property values and aliases for this property.
   *
   * @return {@code null} if the property has no values.
   */
  @Nullable
  public Multimap<String, String> getPropertyValueAliases(String propName) {
    return allPropertyValueAliases.get(propName);
  }

  public Collection<String> getPropertyValueAliases(String propName, String propValue) {
    Multimap<String, String> aliases = getPropertyValueAliases(propName);
    if (aliases == null) {
      return ImmutableSet.of(propValue);
    }
    return aliases.get(getCanonicalValueName(propName, propValue));
  }

  public String getCanonicalValueName(String normalizedPropName, String propValue) {
    Map<String, String> canonicalPropValueNames =
            propertyValueAlias2CanonicalValue.get(normalizedPropName);
    String canonicalValue = PropertyNameNormalizer.normalize(propValue);
    if (canonicalPropValueNames == null) {
      return canonicalValue;
    }
    return canonicalPropValueNames.get(canonicalValue);
  }

  public ImmutableSet<String> getPropertyNames() {
    return ImmutableSet.copyOf(allPropertyValueAliases.keySet());
  }
}
