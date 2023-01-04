/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeDigitFlexTemplateVars extends UnicodeVersionTemplateVars {
  /** Regex symbol used for the digit property. */
  public String symbol;
  /** Whether the symbol is a digit. */
  public boolean value;
}
