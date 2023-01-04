/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcompat;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.scanner.UcdScanner;
import de.jflex.ucd_generator.scanner.UcdScannerException;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.velocity.runtime.parser.ParseException;

public class CompatPropertiesTestGenerator {

  private CompatPropertiesTestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    UcdVersion ucdVersion =
        UcdVersion.findUcdFiles(
            version.version(), ImmutableList.copyOf(Arrays.copyOfRange(args, 1, args.length)));
    UnicodeData unicodeData = parseUcd(ucdVersion);
    generate(version, unicodeData, outDir);
  }

  private static void generate(UnicodeVersion version, UnicodeData unicodeData, Path outDir)
      throws IOException, ParseException {
    new UnicodeCompatibilityPropertiesTestGenerator(version).generate(outDir);
    for (String propName : UnicodeCompatibilityProperties.COMPATIBILITY_PROPERTIES) {
      UnicodeCompatFlexGenerators.create(version, propName).generate(outDir);
      new UnicodeCompatibilityPropertiesGoldenGenerator(version, unicodeData, propName)
          .generate(outDir);
    }
  }

  private static UnicodeData parseUcd(UcdVersion ucdVersion) throws UcdScannerException {
    UcdScanner scanner = new UcdScanner(ucdVersion);
    UnicodeData unicodeData = scanner.scan();
    return unicodeData;
  }
}
