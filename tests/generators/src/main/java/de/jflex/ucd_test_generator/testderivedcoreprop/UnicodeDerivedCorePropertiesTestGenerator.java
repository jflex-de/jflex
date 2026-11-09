/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testderivedcoreprop;

import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

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
