package jflex.ucd_generator.scanner.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jflex.ucd_generator.util.PropertyNameNormalizer;

@AutoValue
public abstract class PropertyValues {
  abstract ImmutableMap<String, ImmutableMultimap<String, String>> allPropertyValueAliases();

  public static Builder builder() {
    return new AutoValue_PropertyValues.Builder();
  }

  public Builder toBuilder() {
    Builder builder = builder();
    for (Map.Entry<String, ImmutableMultimap<String, String>> entry :
        allPropertyValueAliases().entrySet()) {
      builder.mAllPropertyValueAliases.put(entry.getKey(), HashMultimap.create(entry.getValue()));
    }
    return builder;
  }

  @AutoValue.Builder
  public abstract static class Builder {
    abstract ImmutableMap.Builder<String, ImmutableMultimap<String, String>>
        allPropertyValueAliasesBuilder();

    abstract PropertyValues internalBuild();

    PropertyValues build() {
      for (Map.Entry<String, Multimap<String, String>> entry :
          mAllPropertyValueAliases.entrySet()) {
        allPropertyValueAliasesBuilder()
            .put(entry.getKey(), ImmutableMultimap.copyOf(entry.getValue()));
      }
      return internalBuild();
    }

    /**
     * Stores all defined property value aliases.
     *
     * <p>property name: property value canonical name: aliases
     */
    Map<String, Multimap<String, String>> mAllPropertyValueAliases = new HashMap<>();

    /** Maps property value aliases to their corresponding canonical property values */
    Map<String, Map<String, String>> mPropertyValueAlias2CanonicalValue = new HashMap<>();

    public void put(String propertyName, String normalizedPropertyValue, Set<String> aliases) {
      Multimap<String, String> aliasesForName = mAllPropertyValueAliases.get(propertyName);
      if (aliasesForName == null) {
        aliasesForName = HashMultimap.create();
        mAllPropertyValueAliases.put(propertyName, aliasesForName);
      }
      aliasesForName.putAll(normalizedPropertyValue, aliases);

      Map<String, String> aliasMap =
          mPropertyValueAlias2CanonicalValue.computeIfAbsent(propertyName, k -> new HashMap());
      for (String propertyValueAlias : aliases) {
        aliasMap.put(PropertyNameNormalizer.normalize(propertyValueAlias), normalizedPropertyValue);
      }
    }
  }
}
