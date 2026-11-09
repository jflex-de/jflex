/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testblock;

import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeBlocksTestJavaTemplateVars extends UnicodeVersionTemplateVars {
  /** The expected blocks as {@code NamedCodepointRange} expressions, sorted by range. */
  public String blockRanges;
}
