/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testcaseless;

import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

public class UnicodeCaselessFlexTemplateVars extends UnicodeVersionTemplateVars {

  /** One scanner rule per codepoint which has caseless equivalences. */
  public String caselessRules;
}
