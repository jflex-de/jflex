/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testdigit;

import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;
import java.util.List;

public class UnicodeDigitGoldenTemplateVars extends UnicodeVersionTemplateVars {

  public List<NamedCodepointRange<Boolean>> digitBlocks;
}
