/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testdigit;

import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeDigitFlexTemplateVars extends UnicodeVersionTemplateVars {
  /** Regex symbol used for the digit property. */
  public String symbol;

  /** Whether the symbol is a digit. */
  public boolean value;
}
