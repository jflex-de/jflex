/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testemoji;

import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UnicodeEmojiTestGenerator extends AbstractGenerator<UnicodeEmojiTestTemplateVars> {

  private static final String TEMPLATE_NAME = "UnicodeEmojiTest.java";

  private final Set<String> propertyNames;

  protected UnicodeEmojiTestGenerator(UnicodeVersion unicodeVersion, Set<String> propertyNames) {
    super(TEMPLATE_NAME, unicodeVersion);
    this.propertyNames = propertyNames;
  }

  @Override
  protected UnicodeEmojiTestTemplateVars createTemplateVars() {
    UnicodeEmojiTestTemplateVars vars = new UnicodeEmojiTestTemplateVars();
    vars.className = "UnicodeEmojiTest_" + unicodeVersion.underscoreVersion();
    List<String> tests = new ArrayList<>();
    for (String propName : propertyNames) {
      String scanner = "UnicodeEmoji_" + propName + "_" + unicodeVersion.underscoreVersion();
      tests.add(
          String.format(
              "  /** Test property {@code %1$s}. */\n"
                  + "  @Test\n"
                  + "  public void emoji_%1$s() throws Exception {\n"
                  + "    %2$s scanner =\n"
                  + "        UnicodeDataScanners.scanAllCodepoints(\n"
                  + "            ScannerFactory.of(%2$s::new),\n"
                  + "            %2$s.YYEOF,\n"
                  + "            UnicodeDataScanners.Dataset.ALL);\n"
                  + "    assertEquals(readGolden(\"%1$s\"), scanner.ranges());\n"
                  + "  }",
              propName, scanner));
    }
    vars.emojiTests = String.join("\n", tests);
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeEmojiTestTemplateVars vars) {
    return vars.className + ".java";
  }
}
