/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.emitter.unicode_properties;

import com.google.common.base.Joiner;
import de.jflex.ucd_generator.emitter.common.UcdEmitter;
import de.jflex.ucd_generator.ucd.UcdVersions;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.velocity.runtime.parser.ParseException;

/** UnicodePropertiesEmitter for {@code UnicodeProperties.java}. */
public class UnicodePropertiesEmitter extends UcdEmitter {

  private static final String TEMPLATE_NAME = "UnicodeProperties.java";
  private static final String UNICODE_PROPERTIES_TEMPLATE =
      JavaPackageUtils.getPathForClass(UnicodePropertiesEmitter.class)
          + "/"
          + TEMPLATE_NAME
          + ".vm";

  private final UcdVersions versions;

  public UnicodePropertiesEmitter(String targetPackage, UcdVersions versions) {
    super(targetPackage);
    this.versions = versions;
  }

  public void emitUnicodeProperties(OutputStream output) throws IOException, ParseException {
    UnicodePropertiesVars unicodePropertiesVars = createUnicodePropertiesVars();
    Velocity.render(
        readResource(UNICODE_PROPERTIES_TEMPLATE), TEMPLATE_NAME, unicodePropertiesVars, output);
  }

  private UnicodePropertiesVars createUnicodePropertiesVars() {
    UnicodePropertiesVars unicodePropertiesVars = new UnicodePropertiesVars();
    unicodePropertiesVars.templateName = TEMPLATE_NAME;
    unicodePropertiesVars.packageName = getTargetPackage();
    unicodePropertiesVars.versionsAsString = Joiner.on(", ").join(versions.expandAllVersions());
    unicodePropertiesVars.latestVersion = versions.getLastVersion().toMajorMinorString();
    unicodePropertiesVars.versions = versions.versionsAsList();
    unicodePropertiesVars.ucdVersions = versions;
    return unicodePropertiesVars;
  }
}
