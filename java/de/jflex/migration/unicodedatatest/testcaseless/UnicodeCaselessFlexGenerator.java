/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import static com.google.common.collect.ImmutableList.toImmutableList;

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
    vars.caselessCodepoints =
        caselessCodepoints.stream().map(this::formatCodepoint).collect(toImmutableList());
    return vars;
  }

  private String formatCodepoint(int cp) {
    if (cp <= 0xFFFF) {
      return String.format("%04X", cp);
    } else {
      return String.format("%06X", cp);
    }
  }

  @Override
  protected String getOuputFileName(UnicodeCaselessFlexTemplateVars vars) {
    return vars.className + ".flex";
  }
}
