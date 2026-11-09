/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testcaseless;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.ucd_test_generator.testcaseless.CaselessTestGenerator.Equivalences;
import java.util.Map;

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
  protected String render(UnicodeCaselessGoldenTemplateVars vars) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Integer, Integer> c : vars.equivalences.entrySet()) {
      sb.append(
          String.format(
              "input char %04X matches %04X case-insensitively\n", c.getKey(), c.getValue()));
    }
    return sb.toString();
  }

  @Override
  protected String getOuputFileName(UnicodeCaselessGoldenTemplateVars vars) {
    return vars.className + ".output";
  }
}
