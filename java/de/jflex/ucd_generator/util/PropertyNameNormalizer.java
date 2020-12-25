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
package de.jflex.ucd_generator.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class PropertyNameNormalizer {

  /** Pattern used to normalize property value identifiers */
  private static final Pattern WORD_SEP_PATTERN = Pattern.compile("[-_\\s()]");

  // "cache"
  private static final Map<String, String> normalized = new HashMap<>();

  /** Normalized General_Category property name. */
  public static final String NORMALIZED_GENERAL_CATEGORY = normalize("General_Category");
  /** Normalized Script property name. */
  public static final String NORMALIZED_SCRIPT = normalize("Script");
  /** Normalized Scirpt_Extensions property name. */
  public static final String NORMALIZED_SCRIPT_EXTENSIONS = normalize("Script_Extensions");

  public static final ImmutableSet<String> DEFAULT_CATEGORIES =
      ImmutableSet.of(NORMALIZED_GENERAL_CATEGORY, NORMALIZED_SCRIPT);

  /** Maps a canonical name to its aliases. */
  private final Multimap<String, String> propertyAliases = HashMultimap.create();
  /** Maps an alias to its canonical name. */
  private final Map<String, String> propertyAlias2CanonicalName = new HashMap<>();

  /**
   * Transforms mixed case identifiers containing spaces, hyphens, and/or underscores by downcasing
   * and removing all spaces, hyphens, underscores, and parentheses; also, converts property
   * name/value separator ':' to '='.
   *
   * @param identifier The identifier to transform
   * @return The transformed identifier
   */
  public static String normalize(String identifier) {
    if (identifier == null) {
      return "";
    }
    return normalized.computeIfAbsent(
        identifier,
        k ->
            WORD_SEP_PATTERN
                .matcher(k.toLowerCase(Locale.ENGLISH))
                .replaceAll("")
                .replace(':', '='));
  }

  public static String canonicalValue(String propName, String propValue) {
    return DEFAULT_CATEGORIES.contains(propName) ? propValue : propName + "=" + propValue;
  }

  /**
   * For the given property name or alias, returns the canonical property name. If none has been
   * encountered, then the given propertyAlias itself is returned.
   *
   * @param propertyAlias The property name or alias for which to lookup the canonical property
   *     name.
   * @return the canonical property name for the given property name or alias. If none has been
   *     encountered, then the given propertyAlias is returned.
   */
  public String getCanonicalPropertyName(String propertyAlias) {
    String normalizedAlias = normalize(propertyAlias);
    return propertyAlias2CanonicalName.getOrDefault(normalizedAlias, normalizedAlias);
  }

  public Collection<String> getPropertyAliases(String propName) {
    return propertyAliases.get(PropertyNameNormalizer.normalize(propName));
  }

  public void putPropertyAlias(String canonicalName, String alias) {
    propertyAliases.put(canonicalName, alias);
    propertyAlias2CanonicalName.put(alias, canonicalName);
  }

  public ImmutableList<String> getCanonicalNames() {
    return ImmutableList.sortedCopyOf(propertyAliases.keySet());
  }
}
