/*
 * Copyright (C) 2014-2020 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2020 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.ucd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class PropertyNames {

  /** Pattern used to normalize property value identifiers */
  private static final Pattern WORD_SEP_PATTERN = Pattern.compile("[-_\\s()]");

  /** Cache of {@link #normalize}. Shared by tests running in parallel. */
  private static final Map<String, String> normalized = new ConcurrentHashMap<>();

  /** Normalized General_Category property name. */
  public static final String NORMALIZED_GENERAL_CATEGORY = normalize("General_Category");

  /** Normalized Script property name. */
  public static final String NORMALIZED_SCRIPT = normalize("Script");

  /** Normalized Scirpt_Extensions property name. */
  public static final String NORMALIZED_SCRIPT_EXTENSIONS = normalize("Script_Extensions");

  public static final Set<String> DEFAULT_CATEGORIES =
      Collections.unmodifiableSet(
          new HashSet<>(Arrays.asList(NORMALIZED_GENERAL_CATEGORY, NORMALIZED_SCRIPT)));

  /** Maps a canonical name to its aliases. */
  private final Map<String, Set<String>> propertyAliases = new HashMap<>();

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
    return propertyAliases.getOrDefault(PropertyNames.normalize(propName), Collections.emptySet());
  }

  public void putPropertyAlias(String canonicalName, String alias) {
    propertyAliases.computeIfAbsent(canonicalName, k -> new HashSet<>()).add(alias);
    propertyAlias2CanonicalName.put(alias, canonicalName);
  }
}
