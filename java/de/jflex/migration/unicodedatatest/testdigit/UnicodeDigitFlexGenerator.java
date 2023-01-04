/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;

public class UnicodeDigitFlexGenerator extends AbstractGenerator<UnicodeDigitFlexTemplateVars> {

  private final String symbol;

  protected UnicodeDigitFlexGenerator(UnicodeVersion unicodeVersion, String symbol) {
    super("UnicodeDigit.flex", unicodeVersion);
    this.symbol = symbol;
  }

  @Override
  protected UnicodeDigitFlexTemplateVars createTemplateVars() {
    UnicodeDigitFlexTemplateVars vars = new UnicodeDigitFlexTemplateVars();
    vars.value = true;
    // Filesystem safe name
    String testName =
        symbol
            .replace('[', '-')
            .replace(']', '-')
            .replace(':', '-')
            .replace('\\', '-')
            .replaceAll("-", "");
    // Work around Bazel confusion of lower/upper case targets
    if (testName.length() == 1) {
      char c = testName.charAt(0);
      if (Character.isLowerCase(c)) {
        testName = "lower" + Character.toUpperCase(c);
      } else {
        // \D means not digit
        vars.value = false;
        testName = "upper" + c;
      }
    }
    vars.className = "UnicodeDigit_" + testName + "_" + unicodeVersion.underscoreVersion();
    vars.symbol = symbol;
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeDigitFlexTemplateVars vars) {
    return vars.className + ".flex";
  }
}
