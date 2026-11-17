/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testcompat;

import java.util.Arrays;
import java.util.List;

/** Constant holder. */
public class UnicodeCompatibilityProperties {

  public static final List<String> COMPATIBILITY_PROPERTIES =
      Arrays.asList("alnum", "blank", "graph", "print", "xdigit");

  private UnicodeCompatibilityProperties() {}
}
