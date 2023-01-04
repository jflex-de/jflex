/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator;

import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.emitter.unicode_properties.UnicodePropertiesEmitter;
import de.jflex.ucd_generator.emitter.unicode_version.UnicodeVersionEmitter;
import de.jflex.ucd_generator.scanner.UcdScanner;
import de.jflex.ucd_generator.scanner.UcdScannerException;
import de.jflex.ucd_generator.ucd.UcdVersions;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.version.Version;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.velocity.runtime.parser.ParseException;

/**
 * Generates {@code UnicodeProperties.java} and the associated {Unicode_X_Y} from Unicode.org data
 * files.
 */
public class UcdGenerator {

  private static final String PACKAGE_JFLEX_UNICODE = "jflex.core.unicode";

  /**
   * Generates {@code UnicodeProperties} and {Unicode_X_Y} from {@code //third_party/unicode_ucd_X}.
   */
  public static void generate(UcdGeneratorParams params)
      throws IOException, ParseException, UcdScannerException {
    UcdVersions ucdVersions = params.ucdVersions();
    File outputDir = params.outputDir();
    Files.createDirectories(outputDir.toPath());
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

  /** Emits {@code Unicode_X_Y.java} files. */
  private static void emitAllUnicodeXY(UcdVersions ucdVersions, File outputDir)
      throws IOException, ParseException, UcdScannerException {
    for (Version version : ucdVersions.versionSet()) {
      UcdVersion ucdVersion = ucdVersions.get(version);
      emitUnicodeVersionXY(ucdVersion, outputDir);
    }
  }

  /** Emits {@code Unicode_X_Y.java} for a give version. */
  static void emitUnicodeVersionXY(UcdVersion ucdVersion, File outputDir)
      throws IOException, ParseException, UcdScannerException {
    String unicodeClassName = ucdVersion.version().unicodeClassName();
    System.out.println(String.format("Emitting %s [WIP]", unicodeClassName));
    UnicodeData unicodeData = scanUnicodeVersion(ucdVersion);
    File outputFile = new File(outputDir, unicodeClassName + ".java");
    UnicodeVersionEmitter emitter =
        new UnicodeVersionEmitter(PACKAGE_JFLEX_UNICODE, ucdVersion, unicodeData);
    try (FileOutputStream out = new FileOutputStream(outputFile)) {
      emitter.emitUnicodeVersion(out);
    }
  }

  private static UnicodeData scanUnicodeVersion(UcdVersion ucdVersion) throws UcdScannerException {
    return new UcdScanner(ucdVersion).scan();
  }

  private UcdGenerator() {}
}
