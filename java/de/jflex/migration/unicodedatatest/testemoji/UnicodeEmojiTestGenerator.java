/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testemoji;

import com.google.common.collect.ImmutableSet;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;

public class UnicodeEmojiTestGenerator extends AbstractGenerator<UnicodeEmojiTestTemplateVars> {

  private static final String TEMPLATE_NAME = "UnicodeEmojiTest.java";

  private final ImmutableSet<String> propertyNames;

  protected UnicodeEmojiTestGenerator(
      UnicodeVersion unicodeVersion, ImmutableSet<String> propertyNames) {
    super(TEMPLATE_NAME, unicodeVersion);
    this.propertyNames = propertyNames;
  }

  @Override
  protected UnicodeEmojiTestTemplateVars createTemplateVars() {
    UnicodeEmojiTestTemplateVars vars = new UnicodeEmojiTestTemplateVars();
    vars.className = "UnicodeEmojiTest_" + unicodeVersion.underscoreVersion();
    vars.propertyNames = propertyNames;
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeEmojiTestTemplateVars vars) {
    return vars.className + ".java";
  }
}
