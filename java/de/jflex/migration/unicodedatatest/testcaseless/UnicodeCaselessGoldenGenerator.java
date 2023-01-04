/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testcaseless;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.testcaseless.CaselessTestGenerator.Equivalences;

public class UnicodeCaselessGoldenGenerator
    extends AbstractGenerator<UnicodeCaselessGoldenTemplateVars> {

  private final Equivalences<Integer> equivalences;

  UnicodeCaselessGoldenGenerator(
      UnicodeVersion unicodeVersion, Equivalences<Integer> equivalences) {
    super("UnicodeCaselessGolden.txt", unicodeVersion);
    this.equivalences = equivalences;
  }

  @Override
  protected UnicodeCaselessGoldenTemplateVars createTemplateVars() {

    UnicodeCaselessGoldenTemplateVars vars = new UnicodeCaselessGoldenTemplateVars();
    vars.className = "UnicodeCaseless_" + unicodeVersion.underscoreVersion();
    vars.equivalences =
        ImmutableList.sortedCopyOf(equivalences.getKeys()).stream()
            .collect(toImmutableMap(identity(), equivalences::getEquivalentValue));
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeCaselessGoldenTemplateVars vars) {
    return vars.className + ".output";
  }
}
