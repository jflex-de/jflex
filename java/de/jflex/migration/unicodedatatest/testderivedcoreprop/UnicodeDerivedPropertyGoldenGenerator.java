/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testderivedcoreprop;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeRangesGoldenTemplateVars;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.CodepointRange;

public class UnicodeDerivedPropertyGoldenGenerator
    extends AbstractGenerator<UnicodeRangesGoldenTemplateVars> {

  private final String testName;
  private final ImmutableList<CodepointRange> ranges;

  public UnicodeDerivedPropertyGoldenGenerator(
      UnicodeVersion version, String testName, ImmutableList<CodepointRange> ranges) {
    super("UnicodeRangesGolden", version);
    this.testName = testName;
    this.ranges = ranges;
  }

  @Override
  protected UnicodeRangesGoldenTemplateVars createTemplateVars() {
    UnicodeRangesGoldenTemplateVars vars = new UnicodeRangesGoldenTemplateVars();
    vars.className = testName;
    vars.ranges = ranges;
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeRangesGoldenTemplateVars vars) {
    return testName + ".output";
  }
}
