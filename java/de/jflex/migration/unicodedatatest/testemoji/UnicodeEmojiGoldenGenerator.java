/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testemoji;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeRangesGoldenTemplateVars;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.ucd_generator.ucd.UnicodeData;

public class UnicodeEmojiGoldenGenerator extends AbstractGenerator<UnicodeVersionTemplateVars> {

  private final UnicodeData unicodeData;
  private final String propertyName;

  protected UnicodeEmojiGoldenGenerator(
      UnicodeVersion unicodeVersion, UnicodeData unicodeData, String propertyName) {
    super("UnicodeRangesGolden", unicodeVersion);
    this.unicodeData = unicodeData;
    this.propertyName = propertyName;
  }

  @Override
  protected UnicodeVersionTemplateVars createTemplateVars() {
    UnicodeRangesGoldenTemplateVars vars = new UnicodeRangesGoldenTemplateVars();
    vars.className = "UnicodeEmoji_" + propertyName + "_" + unicodeVersion.underscoreVersion();
    vars.ranges = unicodeData.getPropertyValueIntervals(propertyName);
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeVersionTemplateVars vars) {
    return vars.className + ".output";
  }
}
