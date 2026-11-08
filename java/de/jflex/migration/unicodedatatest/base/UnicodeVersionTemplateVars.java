/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.base;

import de.jflex.template.TemplateVars;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.ucd.Versions;
import de.jflex.version.Version;

public class UnicodeVersionTemplateVars extends TemplateVars {
  /** The class name produced by this Java template. */
  public String className;

  /** java package with '.', used by the scanner. */
  public String javaPackage;

  /** The unicode version under test. */
  public Version unicodeVersion;

  /** The unicode version under test with '_' as separator, e.g. {@code 12_1}. */
  public String underscoreVersion;

  /** The maximum codepoint for this Unicode version. */
  public int maxCodePoint;

  /** The dataset to use, e.g. {@code Ages.Dataset.BMP} */
  public UnicodeDataScanners.Dataset dataset;

  public void updateFrom(UnicodeVersion version) {
    if (javaPackage == null) {
      javaPackage = version.javaPackage();
    }
    if (unicodeVersion == null) {
      unicodeVersion = version.version();
    }
    if (underscoreVersion == null) {
      underscoreVersion = unicodeVersion.underscoreVersion();
    }
    if (maxCodePoint == 0) {
      maxCodePoint = Versions.maxCodePoint(version.version());
    }
    if (dataset == null) {
      dataset = UnicodeDataScanners.getDataset(version.version());
    }
  }
}
