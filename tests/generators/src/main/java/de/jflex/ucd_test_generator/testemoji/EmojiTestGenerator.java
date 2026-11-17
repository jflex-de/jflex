/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testemoji;

import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.scanner.UcdScanner;
import de.jflex.ucd_generator.scanner.UcdScannerException;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_test_generator.base.UnicodePropertyFlexGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class EmojiTestGenerator {

  public static final Set<String> EMOJI_PROPERTIES =
      Collections.unmodifiableSet(
          new LinkedHashSet<>(
              Arrays.asList(
                  "Emoji", "Emoji_Modifier", "Emoji_Modifier_Base", "Emoji_Presentation")));

  private EmojiTestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    UcdVersion ucdVersion =
        UcdVersion.findUcdFiles(
            version.version(), Arrays.asList(Arrays.copyOfRange(args, 2, args.length)));
    UnicodeData unicodeData = parseUcd(ucdVersion);
    generate(version, unicodeData, outDir);
  }

  private static void generate(UnicodeVersion version, UnicodeData unicodeData, Path outDir)
      throws IOException {
    Set<String> emojiProperties = propertiesForVersion(version);
    new UnicodeEmojiTestGenerator(version, emojiProperties).generate(outDir);
    for (String propName : emojiProperties) {
      UnicodePropertyFlexGenerator.createPropertyScanner(
              version, "UnicodeEmoji_" + propName + "_" + version.underscoreVersion(), propName)
          .generate(outDir);
      new UnicodeEmojiGoldenGenerator(version, unicodeData, propName).generate(outDir);
    }
  }

  private static UnicodeData parseUcd(UcdVersion ucdVersion) throws UcdScannerException {
    UcdScanner scanner = new UcdScanner(ucdVersion);
    return scanner.scan();
  }

  public static Set<String> propertiesForVersion(UnicodeVersion unicodeVersion) {
    Set<String> properties = new LinkedHashSet<>(EMOJI_PROPERTIES);
    if (unicodeVersion.version().major >= 11) {
      properties.add("Extended_Pictographic");
    }
    if (unicodeVersion.version().major >= 10) {
      properties.add("Emoji_Component");
    }
    return Collections.unmodifiableSet(properties);
  }
}
