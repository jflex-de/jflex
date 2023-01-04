/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.base;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;

public class UnicodeRangesGoldenTemplateVars extends UnicodeVersionTemplateVars {
  public ImmutableList<CodepointRange> ranges;
}
