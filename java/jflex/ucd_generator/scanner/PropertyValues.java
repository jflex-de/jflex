package jflex.ucd_generator.scanner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PropertyValues {
  /**
   * Stores all defined property value aliases.
   *
   * <p>property name: property value canonical name: aliases
   */
  Map<String, Multimap<String, String>> allPropertyValueAliases = new HashMap<>();

  /** Maps property value aliases to their corresponding canonical property values */
  Map<String, Map<String, String>> propertyValueAlias2CanonicalValue = new HashMap<>();

  public void put(String propertyName, String normalizedPropertyValue, Set<String> aliases) {
    Multimap<String, String> aliasesForName = allPropertyValueAliases.get(propertyName);
    if (aliasesForName == null) {
      aliasesForName = HashMultimap.create();
      allPropertyValueAliases.put(propertyName, aliasesForName);
    }
    aliasesForName.putAll(normalizedPropertyValue, aliases);

    Map<String, String> aliasMap =
        propertyValueAlias2CanonicalValue.computeIfAbsent(propertyName, k -> new HashMap());
    for (String propertyValueAlias : aliases) {
      aliasMap.put(PropertyNameNormalizer.normalize(propertyValueAlias), normalizedPropertyValue);
    }
  }
}
