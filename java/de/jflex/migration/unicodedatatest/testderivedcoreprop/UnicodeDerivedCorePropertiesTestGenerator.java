/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testderivedcoreprop;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeDerivedCorePropertiesTestGenerator
    extends AbstractGenerator<UnicodeVersionTemplateVars> {

  private static final String TEMPLATE = "UnicodeDerivedCorePropertiesTest.java";

  public UnicodeDerivedCorePropertiesTestGenerator(UnicodeVersion unicodeVersion) {
    super(TEMPLATE, unicodeVersion);
  }

  @Override
  protected UnicodeVersionTemplateVars createTemplateVars() {
    UnicodeVersionTemplateVars vars = new UnicodeVersionTemplateVars();
    vars.className = "UnicodeDerivedCorePropertiesTest_" + unicodeVersion.underscoreVersion();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeVersionTemplateVars vars) {
    return vars.className + ".java";
  }
}
