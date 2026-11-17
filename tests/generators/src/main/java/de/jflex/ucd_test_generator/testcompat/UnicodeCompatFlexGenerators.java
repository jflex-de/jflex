/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testcompat;

import de.jflex.ucd_test_generator.base.UnicodePropertyFlexGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import java.util.Collections;
import java.util.TreeMap;

public class UnicodeCompatFlexGenerators {

  public static UnicodePropertyFlexGenerator<Boolean> create(
      UnicodeVersion version, String propName) {
    String className =
        "UnicodeCompatibilityProperties_" + propName + "_" + version.underscoreVersion();
    return new UnicodePropertyFlexGenerator<>(
        version, className, new TreeMap<>(Collections.singletonMap(propName, true)), Boolean.class);
  }

  private UnicodeCompatFlexGenerators() {}
}
