/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testblock;

import com.google.common.collect.ImmutableSortedSet;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.ucd.NamedCodepointRange;

public class UnicodeBlocksTestJavaTemplateVars extends UnicodeVersionTemplateVars {
  /** Blocks sorted by range. */
  public ImmutableSortedSet<NamedCodepointRange> blocks;
}
