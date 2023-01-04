/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.base;

import java.util.NavigableMap;

public class UnicodePropertyFlexTemplateVars<T> extends UnicodeVersionTemplateVars {
  public NavigableMap<String, T> properties;
  public Class<T> propertyValueClass;
}
