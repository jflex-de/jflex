/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode.unicode_8_0;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.SimpleIntervalsParser;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.ucd.CodepointRange;
import de.jflex.util.scanner.ScannerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Generated;
import org.junit.Test;

// Generate from UnicodeEmojiTest.java.vm
/** Test the emoji property. */
@Generated("de.jflex.migration.unicodedatatest.testemoji.UnicodeEmojiTestGenerator")
public class UnicodeEmojiTest_8_0 {

  private static final Path PACKAGE_DIRECTORY =
      Paths.get("javatests/de/jflex/testcase/unicode").resolve("unicode_8_0");

  private static ImmutableList<CodepointRange> expected;

  private static ImmutableList<CodepointRange> readGolden(String propName) throws Exception {
    Path expectedFile = PACKAGE_DIRECTORY.resolve("UnicodeEmoji_" + propName + "_8_0.output");
    return SimpleIntervalsParser.parseRanges(expectedFile);
  }

  /** Test property {@code Emoji}. */
  @Test
  public void emoji_Emoji() throws Exception {
    UnicodeEmoji_Emoji_8_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_8_0::new),
            UnicodeEmoji_Emoji_8_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji"));
  }
  /** Test property {@code Emoji_Modifier}. */
  @Test
  public void emoji_Emoji_Modifier() throws Exception {
    UnicodeEmoji_Emoji_Modifier_8_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Modifier_8_0::new),
            UnicodeEmoji_Emoji_Modifier_8_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Modifier"));
  }
  /** Test property {@code Emoji_Modifier_Base}. */
  @Test
  public void emoji_Emoji_Modifier_Base() throws Exception {
    UnicodeEmoji_Emoji_Modifier_Base_8_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Modifier_Base_8_0::new),
            UnicodeEmoji_Emoji_Modifier_Base_8_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Modifier_Base"));
  }
  /** Test property {@code Emoji_Presentation}. */
  @Test
  public void emoji_Emoji_Presentation() throws Exception {
    UnicodeEmoji_Emoji_Presentation_8_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Presentation_8_0::new),
            UnicodeEmoji_Emoji_Presentation_8_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Presentation"));
  }
}
