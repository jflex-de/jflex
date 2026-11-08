/*
 * Copyright (C) 2017-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testcompat;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import java.util.ArrayList;
import java.util.List;

public class UnicodeCompatibilityPropertiesTestGenerator
    extends AbstractGenerator<UnicodeCompatibilityPropertiesTestTemplateVars> {

  protected UnicodeCompatibilityPropertiesTestGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeCompatibilityPropertiesTest.java", unicodeVersion);
  }

  @Override
  protected UnicodeCompatibilityPropertiesTestTemplateVars createTemplateVars() {
    UnicodeCompatibilityPropertiesTestTemplateVars vars =
        new UnicodeCompatibilityPropertiesTestTemplateVars();
    vars.className = "UnicodeCompatibilityPropertiesTest_" + unicodeVersion.underscoreVersion();
    List<String> tests = new ArrayList<>();
    for (String prop : UnicodeCompatibilityProperties.COMPATIBILITY_PROPERTIES) {
      String scanner =
          "UnicodeCompatibilityProperties_" + prop + "_" + unicodeVersion.underscoreVersion();
      tests.add(
          String.format(
              "  /** Test the character class syntax of the Unicode %1$s '%2$s' compatibility"
                  + " property. */\n"
                  + "  @Test\n"
                  + "  public void %2$s() throws Exception {\n"
                  + "    checkCompatibility(\n"
                  + "        \"%2$s\",\n"
                  + "        %3$s.class,\n"
                  + "        %3$s::new,\n"
                  + "        %3$s.YYEOF);\n"
                  + "  }",
              unicodeVersion.version(), prop, scanner));
    }
    vars.propertyTests = String.join("\n\n", tests);
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeCompatibilityPropertiesTestTemplateVars vars) {
    return vars.className + ".java";
  }
}
