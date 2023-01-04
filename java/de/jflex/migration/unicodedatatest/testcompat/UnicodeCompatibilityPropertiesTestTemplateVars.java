/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testcompat;

import static de.jflex.migration.unicodedatatest.testcompat.UnicodeCompatibilityProperties.COMPATIBILITY_PROPERTIES;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;

public class UnicodeCompatibilityPropertiesTestTemplateVars extends UnicodeVersionTemplateVars {
  public final ImmutableList<String> properties = COMPATIBILITY_PROPERTIES;
}
