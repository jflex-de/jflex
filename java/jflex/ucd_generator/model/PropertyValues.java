package jflex.ucd_generator.model;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
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
    return checkNotNull(
        canonicalPropValueNames.get(canonicalValue),
        String.format(
            "Canonical value not found for '%s', are you sure it is the default value for '%s' "
                + " in this Unicode version?",
            propValue, normalizedPropName));
  }

  public void copyPropertyValueAliases(String sourceProperty, String destProperty) {
    allPropertyValueAliases.put(destProperty, allPropertyValueAliases.get(sourceProperty));
    propertyValueAlias2CanonicalValue.put(
        destProperty, propertyValueAlias2CanonicalValue.get(sourceProperty));
  }
}
