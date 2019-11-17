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
package jflex.ucd_generator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import jflex.ucd_generator.emitter.unicode_properties.UnicodePropertiesEmitter;
import jflex.ucd_generator.emitter.unicode_version.UnicodeVersionEmitter;
import jflex.ucd_generator.scanner.UnicodeData;
import jflex.ucd_generator.scanner.UnicodeDataScanner;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.UcdVersions;
import jflex.ucd_generator.ucd.Version;
import org.apache.velocity.runtime.parser.ParseException;

public class UcdGenerator {

  private static final String PACKAGE_JFLEX_UNICODE = "jflex.core.unicode";

  /**
   * Generates {@code UnicodeProperties} and {Unicode_X_Y} from {@code //third_party/unicode_ucd_X}.
   */
  public static void generate(UcdGeneratorParams params) throws IOException, ParseException {
    UcdVersions ucdVersions = params.ucdVersions();
    File outputDir = params.outputDir();
    System.out.println("Emitting UnicodeProperties.java");
    emitUnicodeProperties(ucdVersions, outputDir);
    System.out.println("Emitting Unicode versions");
    emitAllUnicodeXY(ucdVersions, outputDir);
  }

  /** Emits {@code UnicodeProperties.java} */
  private static void emitUnicodeProperties(UcdVersions ucdVersions, File outputDir)
      throws IOException, ParseException {
    UnicodePropertiesEmitter propertiesEmitter =
        new UnicodePropertiesEmitter(PACKAGE_JFLEX_UNICODE, ucdVersions);
    File outputFile = new File(outputDir, "UnicodeProperties.java");
    try (FileOutputStream out = new FileOutputStream(outputFile)) {
      propertiesEmitter.emitUnicodeProperties(out);
    }
  }

  /** Emits {@code Unicode_X_Y.java} */
  private static void emitAllUnicodeXY(UcdVersions ucdVersions, File outputDir)
      throws IOException, ParseException {
    for (Version version : ucdVersions.versionSet()) {
      UcdVersion ucdVersion = ucdVersions.get(version);
      emitUnicodeVersionXY(ucdVersion, outputDir);
    }
  }

  private static void emitUnicodeVersionXY(UcdVersion ucdVersion, File outputDir)
      throws IOException, ParseException {
    String unicodeClassName = ucdVersion.version().unicodeClassName();
    System.out.println(String.format("Emitting %s [WIP]", unicodeClassName));
    File dataFile = ucdVersion.getFile(UcdFileType.UnicodeData);
    System.out.println("Opening " + dataFile);
    File outputFile = new File(outputDir, unicodeClassName + ".java");
    UnicodeDataScanner scanner =
        new UnicodeDataScanner(Files.newReader(dataFile, Charsets.UTF_8), ucdVersion);
    scanner.scan();
    UnicodeData unicodeData = scanner.getUnicodeData();
    UnicodeVersionEmitter emitter =
        new UnicodeVersionEmitter(PACKAGE_JFLEX_UNICODE, ucdVersion, unicodeData);
    try (FileOutputStream out = new FileOutputStream(outputFile)) {
      emitter.emitUnicodeVersion(out);
    }
  }

  private UcdGenerator() {}
}
