/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testemoji;

import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_test_generator.base.AbstractRangesGoldenGenerator;
import de.jflex.ucd_test_generator.base.UnicodeRangesGoldenTemplateVars;
import de.jflex.ucd_test_generator.base.UnicodeVersion;

public class UnicodeEmojiGoldenGenerator extends AbstractRangesGoldenGenerator {

  private final UnicodeData unicodeData;
  private final String propertyName;

  protected UnicodeEmojiGoldenGenerator(
      UnicodeVersion unicodeVersion, UnicodeData unicodeData, String propertyName) {
    super(unicodeVersion);
    this.unicodeData = unicodeData;
    this.propertyName = propertyName;
  }

  @Override
  protected UnicodeRangesGoldenTemplateVars createTemplateVars() {
    UnicodeRangesGoldenTemplateVars vars = new UnicodeRangesGoldenTemplateVars();
    vars.className = "UnicodeEmoji_" + propertyName + "_" + unicodeVersion.underscoreVersion();
    vars.ranges = unicodeData.getPropertyValueIntervals(propertyName);
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeRangesGoldenTemplateVars vars) {
    return vars.className + ".output";
  }
}
