/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.emitter.unicode_properties;

import com.google.common.base.Joiner;
import de.jflex.template.Template;
import de.jflex.ucd_generator.emitter.common.UcdEmitter;
import de.jflex.ucd_generator.ucd.UcdVersions;
import de.jflex.util.javac.JavaPackageUtils;
import java.io.IOException;
import java.io.OutputStream;

/** UnicodePropertiesEmitter for {@code UnicodeProperties.java}. */
public class UnicodePropertiesEmitter extends UcdEmitter {

  private static final String TEMPLATE_NAME = "UnicodeProperties.java";
  private static final String UNICODE_PROPERTIES_TEMPLATE =
      JavaPackageUtils.getPathForClass(UnicodePropertiesEmitter.class)
          + "/"
          + TEMPLATE_NAME
          + ".template";

  private final UcdVersions versions;

  public UnicodePropertiesEmitter(String targetPackage, UcdVersions versions) {
    super(targetPackage);
    this.versions = versions;
  }

  public void emitUnicodeProperties(OutputStream output) throws IOException {
    UnicodePropertiesVars unicodePropertiesVars = createUnicodePropertiesVars();
    Template.render(readResource(UNICODE_PROPERTIES_TEMPLATE), unicodePropertiesVars, output);
  }

  private UnicodePropertiesVars createUnicodePropertiesVars() {
    UnicodePropertiesVars unicodePropertiesVars = new UnicodePropertiesVars();
    unicodePropertiesVars.templateName = TEMPLATE_NAME;
    unicodePropertiesVars.packageName = getTargetPackage();
    unicodePropertiesVars.versionsAsString = Joiner.on(", ").join(versions.expandAllVersions());
    unicodePropertiesVars.latestVersion = versions.getLastVersion().toMajorMinorString();
    unicodePropertiesVars.versionCases = versionCases();
    return unicodePropertiesVars;
  }

  /** The cases of the {@code switch} in {@code UnicodeProperties.init}, one group per version. */
  private String versionCases() {
    StringBuilder sb = new StringBuilder();
    String dataPackage = getTargetPackage() + ".data.";
    for (String v : versions.versionsAsList()) {
      for (String ev : UcdVersions.expandVersion(v)) {
        sb.append("      case \"").append(ev).append("\":\n");
      }
      String data = dataPackage + UcdVersions.getClassNameForVersion(v) + ".";
      sb.append("        bind(\n");
      sb.append("            ").append(data).append("propertyValues,\n");
      sb.append("            ").append(data).append("intervals,\n");
      sb.append("            ").append(data).append("propertyValueAliases,\n");
      sb.append("            ").append(data).append("maximumCodePoint,\n");
      sb.append("            ").append(data).append("caselessMatchPartitions,\n");
      sb.append("            ").append(data).append("caselessMatchPartitionSize);\n");
      sb.append("        break;\n");
    }
    // The template has the line break after the last case.
    return sb.substring(0, sb.length() - 1);
  }
}
