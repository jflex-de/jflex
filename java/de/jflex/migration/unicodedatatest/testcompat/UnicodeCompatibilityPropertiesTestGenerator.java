/*
 * Copyright (C) 2017-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testcompat;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;

public class UnicodeCompatibilityPropertiesTestGenerator
    extends AbstractGenerator<UnicodeCompatibilityPropertiesTestTemplateVars> {

  protected UnicodeCompatibilityPropertiesTestGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeCompatibilityPropertiesTest.java", unicodeVersion);
  }

  @Override
  protected UnicodeCompatibilityPropertiesTestTemplateVars createTemplateVars() {
    UnicodeCompatibilityPropertiesTestTemplateVars vars =
        new UnicodeCompatibilityPropertiesTestTemplateVars();
    vars.className = "UnicodeCompatibilityPropertiesTest_" + unicodeVersion.underscoreVersion();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeCompatibilityPropertiesTestTemplateVars vars) {
    return vars.className + ".java";
  }
}
