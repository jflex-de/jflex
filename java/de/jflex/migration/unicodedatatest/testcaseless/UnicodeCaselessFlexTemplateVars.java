/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeCaselessFlexTemplateVars extends UnicodeVersionTemplateVars {

  /** List of hexa representation of codepoints which ahave caseless equivalences, eg "0041". */
  public ImmutableList<String> caselessCodepoints;
}
