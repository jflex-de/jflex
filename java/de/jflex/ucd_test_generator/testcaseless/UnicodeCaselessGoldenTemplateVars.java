/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testcaseless;

import com.google.common.collect.ImmutableMap;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeCaselessGoldenTemplateVars extends UnicodeVersionTemplateVars {
  public ImmutableMap<Integer, Integer> equivalences;
}
