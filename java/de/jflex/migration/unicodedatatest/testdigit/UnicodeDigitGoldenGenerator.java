/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.ucd_generator.ucd.UnicodeData;

public class UnicodeDigitGoldenGenerator extends AbstractGenerator<UnicodeDigitGoldenTemplateVars> {

  private static final String TEMPLATE_NAME = "UnicodeDigitGolden";

  private final UnicodeData unicodeData;

  protected UnicodeDigitGoldenGenerator(UnicodeVersion unicodeVersion, UnicodeData unicodeData) {
    super(TEMPLATE_NAME, unicodeVersion);
    this.unicodeData = unicodeData;
  }

  @Override
  protected UnicodeDigitGoldenTemplateVars createTemplateVars() {
    UnicodeDigitGoldenTemplateVars vars = new UnicodeDigitGoldenTemplateVars();
    vars.className = "UnicodeDigit_" + unicodeVersion.underscoreVersion();
    vars.digitBlocks = createDigitBlocks(Versions.maxCodePoint(unicodeVersion.version()));
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeDigitGoldenTemplateVars vars) {
    return vars.className + ".output";
  }

  private ImmutableList<NamedCodepointRange<Boolean>> createDigitBlocks(int maxCodepoint) {
    ImmutableList<CodepointRange> ranges = unicodeData.getPropertyValueIntervals("Nd");
    DigitBlocks.Builder digitBlocks = DigitBlocks.builder();
    digitBlocks.add(false, 0, ranges.get(0).start() - 1);
    digitBlocks.add(true, ranges.get(0));
    for (int i = 1; i < ranges.size(); i++) {
      digitBlocks.add(false, ranges.get(i - 1).end() + 1, ranges.get(i).start() - 1);
      digitBlocks.add(true, ranges.get(i));
    }
    digitBlocks.add(false, ranges.get(ranges.size() - 1).end() + 1, maxCodepoint);
    return digitBlocks.build().blocks();
  }
}
