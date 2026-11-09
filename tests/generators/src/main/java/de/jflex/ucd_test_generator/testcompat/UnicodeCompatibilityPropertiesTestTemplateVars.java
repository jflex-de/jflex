/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testcompat;

import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeCompatibilityPropertiesTestTemplateVars extends UnicodeVersionTemplateVars {
  /** One test method per compatibility property. */
  public String propertyTests;
}
