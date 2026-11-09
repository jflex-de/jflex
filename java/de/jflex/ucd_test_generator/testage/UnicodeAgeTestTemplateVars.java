/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testage;

import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;
import java.nio.file.Path;

public class UnicodeAgeTestTemplateVars extends UnicodeVersionTemplateVars {
  /** java package directory. */
  public Path javaPackageDir;

  /** The prefix of the names of the scanners. */
  public String scannerPrefix;

  /** One assertion per age up to {@link #unicodeVersion}. */
  public String ageAssertions;

  /** One test method per age up to {@link #unicodeVersion}. */
  public String ageTests;
}
