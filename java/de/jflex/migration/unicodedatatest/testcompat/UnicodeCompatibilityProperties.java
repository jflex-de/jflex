/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testcompat;

import com.google.common.collect.ImmutableList;

/** Constant holder. */
public class UnicodeCompatibilityProperties {

  public static final ImmutableList<String> COMPATIBILITY_PROPERTIES =
      ImmutableList.of("alnum", "blank", "graph", "print", "xdigit");

  private UnicodeCompatibilityProperties() {}
}
