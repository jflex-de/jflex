/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testage;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.version.Version;
import java.nio.file.Path;

public class UnicodeAgeTestTemplateVars extends UnicodeVersionTemplateVars {
  /** java package directory. */
  public Path javaPackageDir;
  /** The prefix of the names of the scanners. */
  public String scannerPrefix;
  /** List of ages up to {@link #unicodeVersion}. */
  public ImmutableList<Version> ages;
}
