/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testage;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.Pair;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.version.Version;

public class UnicodeAgeSubtractionTemplateVars extends UnicodeVersionTemplateVars {
  public ImmutableList<Pair<Version>> ages;
}
