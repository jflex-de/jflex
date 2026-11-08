/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeCaselessFlexTemplateVars extends UnicodeVersionTemplateVars {

  /** One scanner rule per codepoint which has caseless equivalences. */
  public String caselessRules;
}
