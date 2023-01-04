/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testcompat;

import com.google.common.collect.ImmutableSortedMap;
import de.jflex.migration.unicodedatatest.base.UnicodePropertyFlexGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;

public class UnicodeCompatFlexGenerators {

  public static UnicodePropertyFlexGenerator<Boolean> create(
      UnicodeVersion version, String propName) {
    String className =
        "UnicodeCompatibilityProperties_" + propName + "_" + version.underscoreVersion();
    return new UnicodePropertyFlexGenerator<>(
        version, className, ImmutableSortedMap.of(propName, true), Boolean.class);
  }

  private UnicodeCompatFlexGenerators() {}
}
