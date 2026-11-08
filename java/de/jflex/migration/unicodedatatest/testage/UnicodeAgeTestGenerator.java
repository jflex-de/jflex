/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testage;

import static de.jflex.ucd.Versions.VERSION_3_1;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.version.Version;
import java.nio.file.Paths;

class UnicodeAgeTestGenerator extends AbstractGenerator<UnicodeAgeTestTemplateVars> {

  private final ImmutableList<Version> ages;

  public UnicodeAgeTestGenerator(UnicodeVersion unicodeVersion, ImmutableList<Version> ages) {
    super("UnicodeAgeTest_x_y.java", unicodeVersion);
    this.ages = ages;
  }

  @Override
  protected UnicodeAgeTestTemplateVars createTemplateVars() {
    UnicodeAgeTestTemplateVars vars = new UnicodeAgeTestTemplateVars();
    vars.dataset = getDataset(unicodeVersion.version());
    vars.className = "UnicodeAgeTest_" + unicodeVersion.underscoreVersion();
    vars.scannerPrefix = "UnicodeAge_" + unicodeVersion.underscoreVersion() + "_age";
    vars.javaPackageDir =
        Paths.get(JavaPackageUtils.getPathForPackage(unicodeVersion.javaPackage()));
    StringBuilder assertions = new StringBuilder();
    StringBuilder tests = new StringBuilder();
    for (Version age : ages) {
      if (assertions.length() > 0) {
        assertions.append('\n');
        tests.append("\n\n");
      }
      assertions.append(
          String.format(
              "    assertThat(properties.getPropertyValues()).contains(\"age=%s\");", age));
      String scanner = vars.scannerPrefix + "_" + age.underscoreVersion();
      tests.append(
          String.format(
              "  /**\n"
                  + "   * Tests character class syntax of the Unicode %1$s\n"
                  + "   * Age=%2$s property.\n"
                  + "   */\n"
                  + "  @Test\n"
                  + "  public void ageIntervals_%3$s() throws Exception {\n"
                  + "    assertAgeInterval(\n"
                  + "        ScannerFactory.of(%4$s::new),\n"
                  + "        %4$s.YYEOF,\n"
                  + "        \"%4$s.output\");\n"
                  + "  }",
              unicodeVersion.version(), age, age.underscoreVersion(), scanner));
    }
    vars.ageAssertions = assertions.toString();
    vars.ageTests = tests.toString();
    return vars;
  }

  private static Dataset getDataset(Version version) {
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, VERSION_3_1) < 0) {
      return Dataset.BMP;
    } else {
      return Dataset.ALL;
    }
  }

  @Override
  protected String getOuputFileName(UnicodeAgeTestTemplateVars vars) {
    return String.format("UnicodeAgeTest_%s.java", unicodeVersion.underscoreVersion());
  }
}
