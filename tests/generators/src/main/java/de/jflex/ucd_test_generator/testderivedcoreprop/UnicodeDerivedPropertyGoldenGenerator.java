/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testderivedcoreprop;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd_test_generator.base.AbstractRangesGoldenGenerator;
import de.jflex.ucd_test_generator.base.UnicodeRangesGoldenTemplateVars;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import java.util.List;

public class UnicodeDerivedPropertyGoldenGenerator extends AbstractRangesGoldenGenerator {

  private final String testName;
  private final List<CodepointRange> ranges;

  public UnicodeDerivedPropertyGoldenGenerator(
      UnicodeVersion version, String testName, List<CodepointRange> ranges) {
    super(version);
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
