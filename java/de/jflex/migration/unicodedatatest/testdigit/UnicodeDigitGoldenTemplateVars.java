/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.ucd.NamedCodepointRange;

public class UnicodeDigitGoldenTemplateVars extends UnicodeVersionTemplateVars {

  public ImmutableList<NamedCodepointRange<Boolean>> digitBlocks;
}
