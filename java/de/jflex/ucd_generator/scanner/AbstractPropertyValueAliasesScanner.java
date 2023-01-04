/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.PropertyNames;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.HashSet;
import java.util.Set;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
abstract class AbstractPropertyValueAliasesScanner {

  private final UnicodeData unicodeData;

  protected final Set<String> aliases = new HashSet<>();

  protected String propertyAlias;
  protected String propertyValue;

  public AbstractPropertyValueAliasesScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  /** Populates the property values and property value aliases for a property. */
  void addPropertyValueAliases() {
    addPropertyValueAliases(
        unicodeData.getCanonicalPropertyName(propertyAlias),
        PropertyNames.normalize(propertyValue));
  }

  private void addPropertyValueAliases(
      String canonicalPropertyName, String normalizedPropertyValue) {
    aliases.add(normalizedPropertyValue);
    unicodeData.addPropertyValueAliases(canonicalPropertyName, normalizedPropertyValue, aliases);
    aliases.clear();
  }
}
