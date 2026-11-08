/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.testcaseless.CaselessTestGenerator.Equivalences;

public class UnicodeCaselessFlexGenerator
    extends AbstractGenerator<UnicodeCaselessFlexTemplateVars> {

  private final ImmutableList<Integer> caselessCodepoints;

  UnicodeCaselessFlexGenerator(UnicodeVersion unicodeVersion, Equivalences<Integer> equivalences) {
    super("UnicodeCaseless.flex", unicodeVersion);
    this.caselessCodepoints = equivalences.getSortedKeys(Integer::compareTo);
  }

  @Override
  protected UnicodeCaselessFlexTemplateVars createTemplateVars() {
    UnicodeCaselessFlexTemplateVars vars = new UnicodeCaselessFlexTemplateVars();
    vars.className = "UnicodeCaseless_" + unicodeVersion.underscoreVersion();
    StringBuilder rules = new StringBuilder();
    for (int cp : caselessCodepoints) {
      if (rules.length() > 0) {
        rules.append('\n');
      }
      rules.append(
          String.format(
              "\"\\U%1$06X\" { setCurCharPropertyValue(yytext(), yylength(), 0x%1$06X); }", cp));
    }
    vars.caselessRules = rules.toString();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeCaselessFlexTemplateVars vars) {
    return vars.className + ".flex";
  }
}
