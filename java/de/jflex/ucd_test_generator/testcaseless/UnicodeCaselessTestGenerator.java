/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testcaseless;

import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeCaselessTestGenerator extends AbstractGenerator<UnicodeVersionTemplateVars> {

  UnicodeCaselessTestGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeCaselessTest.java", unicodeVersion);
  }

  @Override
  protected UnicodeVersionTemplateVars createTemplateVars() {
    UnicodeVersionTemplateVars vars = new UnicodeVersionTemplateVars();
    vars.className = "UnicodeCaselessTest_" + unicodeVersion.underscoreVersion();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeVersionTemplateVars vars) {
    return vars.className + ".java";
  }
}
