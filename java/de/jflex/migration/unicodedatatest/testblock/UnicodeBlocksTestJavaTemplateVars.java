/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testblock;

import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeBlocksTestJavaTemplateVars extends UnicodeVersionTemplateVars {
  /** The expected blocks as {@code NamedCodepointRange} expressions, sorted by range. */
  public String blockRanges;
}
