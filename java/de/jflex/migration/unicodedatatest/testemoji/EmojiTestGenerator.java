/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testemoji;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.jflex.migration.unicodedatatest.base.UnicodePropertyFlexGenerator;
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

public class EmojiTestGenerator {

  public static final ImmutableSet<String> EMOJI_PROPERTIES =
      ImmutableSet.of("Emoji", "Emoji_Modifier", "Emoji_Modifier_Base", "Emoji_Presentation");

  private EmojiTestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    UcdVersion ucdVersion =
        UcdVersion.findUcdFiles(
            version.version(), ImmutableList.copyOf(Arrays.copyOfRange(args, 2, args.length)));
    UnicodeData unicodeData = parseUcd(ucdVersion);
    generate(version, unicodeData, outDir);
  }

  private static void generate(UnicodeVersion version, UnicodeData unicodeData, Path outDir)
      throws IOException, ParseException {
    ImmutableSet<String> emojiProperties = propertiesForVersion(version);
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

  public static ImmutableSet<String> propertiesForVersion(UnicodeVersion unicodeVersion) {
    ImmutableSet.Builder<String> properties =
        ImmutableSet.<String>builder().addAll(EMOJI_PROPERTIES);
    if (unicodeVersion.version().major >= 11) {
      properties.add("Extended_Pictographic");
    }
    if (unicodeVersion.version().major >= 10) {
      properties.add("Emoji_Component");
    }
    return properties.build();
  }
}
