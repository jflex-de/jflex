/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest;

import static de.jflex.migration.unicodedatatest.testcompat.UnicodeCompatibilityProperties.COMPATIBILITY_PROPERTIES;
import static de.jflex.migration.unicodedatatest.testderivedcoreprop.UnicodeDerivedCoreProperties.DERIVED_CORE_PROPERTIES;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.version.Version;

public class BuildFileTemplateVars extends UnicodeVersionTemplateVars {
  public final ImmutableList<String> compatibilityProperties = COMPATIBILITY_PROPERTIES;
  public final ImmutableList<String> derivedCoreProperties = DERIVED_CORE_PROPERTIES;
  public ImmutableSet<String> emojiProperties;
  public ImmutableList<Version> ages;
}
