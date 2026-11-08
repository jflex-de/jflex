/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testage;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.version.Version;

class UnicodeAgeSubtractionFlexGenerator
    extends AbstractGenerator<UnicodeAgeSubtractionTemplateVars> {

  protected UnicodeAgeSubtractionFlexGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeAgeSubtraction.flex", unicodeVersion);
  }

  @Override
  protected UnicodeAgeSubtractionTemplateVars createTemplateVars() {
    UnicodeAgeSubtractionTemplateVars vars = new UnicodeAgeSubtractionTemplateVars();
    vars.className =
        String.format(
            "UnicodeAge_%s_age_subtraction", unicodeVersion.version().underscoreVersion());
    ImmutableList<Version> ages = olderAges(unicodeVersion.version());
    StringBuilder rules = new StringBuilder();
    for (int i = 1; i < ages.size(); i++) {
      if (rules.length() > 0) {
        rules.append('\n');
      }
      String older = ages.get(i - 1).toString();
      String newer = ages.get(i).toString();
      rules.append(
          String.format(
              "[\\p{Age:%2$s}--\\p{Age:%1$s}] {\n"
                  + "  setCurCharPropertyValue(yytext(), yylength(),"
                  + " \"[\\\\p{Age:%2$s}--\\\\p{Age:%1$s}]\");\n"
                  + "}",
              older, newer));
    }
    vars.subtractionRules = rules.toString();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeAgeSubtractionTemplateVars vars) {
    return vars.className + ".flex";
  }
}
