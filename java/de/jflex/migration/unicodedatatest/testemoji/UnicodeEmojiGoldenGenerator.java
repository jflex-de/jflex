/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testemoji;

import de.jflex.migration.unicodedatatest.base.AbstractRangesGoldenGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeRangesGoldenTemplateVars;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd_generator.ucd.UnicodeData;

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
