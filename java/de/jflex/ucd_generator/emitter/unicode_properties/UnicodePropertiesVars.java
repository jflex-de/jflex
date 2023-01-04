/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.emitter.unicode_properties;

import de.jflex.ucd_generator.ucd.UcdVersions;
import de.jflex.velocity.TemplateVars;

// the fields in this class are read via reflection by Velocity
@SuppressWarnings({"unused", "WeakerAccess"})
public class UnicodePropertiesVars extends TemplateVars {
  public String packageName;
  public String versionsAsString;
  public String latestVersion;
  public Iterable<String> versions;
  public UcdVersions ucdVersions;
}
