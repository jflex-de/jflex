/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testdigit;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeDigitGoldenTemplateVars extends UnicodeVersionTemplateVars {

  public ImmutableList<NamedCodepointRange<Boolean>> digitBlocks;
}
