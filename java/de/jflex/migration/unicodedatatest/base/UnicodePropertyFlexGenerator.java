/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.base;

import com.google.common.collect.ImmutableSortedMap;
import java.util.NavigableMap;

/** Generates the flex of the scanners for a all ages of a given Unicode version. */
public class UnicodePropertyFlexGenerator<T>
    extends AbstractGenerator<UnicodePropertyFlexTemplateVars<T>> {

  private final String className;
  private final NavigableMap<String, T> properties;
  private final Class<T> propertyValueClass;

  public UnicodePropertyFlexGenerator(
      UnicodeVersion unicodeVersion,
      String className,
      NavigableMap<String, T> properties,
      Class<T> propertyValueClass) {
    super("UnicodeProperty.flex", unicodeVersion);
    this.className = className;
    this.properties = properties;
    this.propertyValueClass = propertyValueClass;
  }

  @Override
  protected String getOuputFileName(UnicodePropertyFlexTemplateVars<T> vars) {
    return vars.className + ".flex";
  }

  @Override
  protected UnicodePropertyFlexTemplateVars<T> createTemplateVars() {
    UnicodePropertyFlexTemplateVars<T> vars = new UnicodePropertyFlexTemplateVars<>();
    vars.className = className;
    vars.properties = properties;
    vars.propertyValueClass = propertyValueClass;
    return vars;
  }

  public static UnicodePropertyFlexGenerator<String> createPropertyValueScanner(
      UnicodeVersion version, String className, String propertyName) {
    return new UnicodePropertyFlexGenerator<>(
        version,
        className,
        ImmutableSortedMap.of(propertyName, "\"" + propertyName + "\""),
        String.class);
  }

  public static UnicodePropertyFlexGenerator<Boolean> createPropertyScanner(
      UnicodeVersion version, String className, String propertyName) {
    return new UnicodePropertyFlexGenerator<>(
        version, className, ImmutableSortedMap.of(propertyName, true), Boolean.class);
  }
}
