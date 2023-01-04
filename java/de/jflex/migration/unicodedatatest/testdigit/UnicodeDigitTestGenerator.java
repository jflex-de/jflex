/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeDigitTestGenerator extends AbstractGenerator<UnicodeVersionTemplateVars> {

  UnicodeDigitTestGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeDigitTest.java", unicodeVersion);
  }

  @Override
  protected UnicodeVersionTemplateVars createTemplateVars() {
    UnicodeVersionTemplateVars vars = new UnicodeVersionTemplateVars();
    vars.className = "UnicodeDigitTest_" + unicodeVersion.underscoreVersion();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeVersionTemplateVars vars) {
    return vars.className + ".java";
  }
}
