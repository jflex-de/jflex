/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2019-2020 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.ucd_generator.ucd;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
