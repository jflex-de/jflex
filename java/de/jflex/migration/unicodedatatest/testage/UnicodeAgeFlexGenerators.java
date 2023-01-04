/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testage;

import de.jflex.migration.unicodedatatest.base.UnicodePropertyFlexGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.version.Version;

public class UnicodeAgeFlexGenerators {
  static UnicodePropertyFlexGenerator createForAge(UnicodeVersion unicodeVersion, Version age) {
    String propertyName = "Age:" + age.toString();
    return UnicodePropertyFlexGenerator.createPropertyValueScanner(
        unicodeVersion, className(unicodeVersion, age.underscoreVersion()), propertyName);
  }

  static UnicodePropertyFlexGenerator createForUnassignedAge(UnicodeVersion unicodeVersion) {
    return UnicodePropertyFlexGenerator.createPropertyValueScanner(
        unicodeVersion, className(unicodeVersion, "unassigned"), "Age:Unassigned");
  }

  private static String className(UnicodeVersion unicodeVersion, String age) {
    return String.format("UnicodeAge_%s_age_%s", unicodeVersion.version().underscoreVersion(), age);
  }

  private UnicodeAgeFlexGenerators() {}
}
