/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jflex.ucd_generator.emitter.unicode_properties;

import com.google.common.base.Joiner;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import jflex.ucd_generator.emitter.common.UcdEmitter;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.UcdVersions;
import jflex.util.javac.JavaPackageUtils;
import jflex.velocity.Velocity;
import org.apache.velocity.runtime.parser.ParseException;

/** UnicodePropertiesEmitter for {@code UnicodeProperties.java}. */
public class UnicodePropertiesEmitter extends UcdEmitter {

  private static final String UNICODE_PROPERTIES_TEMPLATE =
      JavaPackageUtils.getPathForClass(UnicodePropertiesEmitter.class)
          + "/UnicodeProperties.java.vm";

  private final UcdVersions versions;

  public UnicodePropertiesEmitter(String targetPackage, UcdVersions versions) {
    super(targetPackage);
    // Hack legacy versions:
    // 1.1, 1.1.5, 2, 2.0, 2.0.14, 2.1, 2.1.9, 3, 3.0, 3.0.1, 3.1, 3.1.0, 3.2, 3.2.0
    this.versions =
        versions.toBuilder()
            .put("1.1.5", UcdVersion.builder("1.1.5").build())
            .put("2.0.14", UcdVersion.builder("2.0.14").build())
            .put("2.1.9", UcdVersion.builder("2.1.9").build())
            .put("3.0.1", UcdVersion.builder("3.0.1").build())
            .put("3.1.0", UcdVersion.builder("3.1.0").build())
            .put("3.2.0", UcdVersion.builder("3.2.0").build())
            .build();
  }

  public void emitUnicodeProperties(OutputStream output) throws IOException, ParseException {
    UnicodePropertiesVars unicodePropertiesVars = createUnicodePropertiesVars();
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      Velocity.render(
          readResource(UNICODE_PROPERTIES_TEMPLATE),
          "UnicodeProperties",
          unicodePropertiesVars,
          writer);
    }
  }

  private UnicodePropertiesVars createUnicodePropertiesVars() {
    UnicodePropertiesVars unicodePropertiesVars = new UnicodePropertiesVars();
    unicodePropertiesVars.packageName = getTargetPackage();
    unicodePropertiesVars.classComment = createClassComment();
    unicodePropertiesVars.versionsAsString = Joiner.on(", ").join(versions.expandAllVersions());
    unicodePropertiesVars.latestVersion = versions.getLastVersion().toMajorMinorString();
    unicodePropertiesVars.versions = versions.versionsAsList();
    unicodePropertiesVars.ucdVersions = versions;
    return unicodePropertiesVars;
  }

  private static String createClassComment() {
    return "// DO NOT EDIT\n"
        + "// This class was automatically generated by //java/jflex/ucd_generator\n"
        + "// based on Unicode data files downloaded from unicode.org.\n"
        + "/**\n"
        + " * Unicode properties.\n"
        + " *\n"
        + " * @author JFlex contributors.\n"
        + " */";
  }
}
