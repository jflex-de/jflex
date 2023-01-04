/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode.unicode_11_0;

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
public class UnicodeEmojiTest_11_0 {

  private static final Path PACKAGE_DIRECTORY =
      Paths.get("javatests/de/jflex/testcase/unicode").resolve("unicode_11_0");

  private static ImmutableList<CodepointRange> expected;

  private static ImmutableList<CodepointRange> readGolden(String propName) throws Exception {
    Path expectedFile = PACKAGE_DIRECTORY.resolve("UnicodeEmoji_" + propName + "_11_0.output");
    return SimpleIntervalsParser.parseRanges(expectedFile);
  }

  /** Test property {@code Emoji}. */
  @Test
  public void emoji_Emoji() throws Exception {
    UnicodeEmoji_Emoji_11_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_11_0::new),
            UnicodeEmoji_Emoji_11_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji"));
  }
  /** Test property {@code Emoji_Modifier}. */
  @Test
  public void emoji_Emoji_Modifier() throws Exception {
    UnicodeEmoji_Emoji_Modifier_11_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Modifier_11_0::new),
            UnicodeEmoji_Emoji_Modifier_11_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Modifier"));
  }
  /** Test property {@code Emoji_Modifier_Base}. */
  @Test
  public void emoji_Emoji_Modifier_Base() throws Exception {
    UnicodeEmoji_Emoji_Modifier_Base_11_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Modifier_Base_11_0::new),
            UnicodeEmoji_Emoji_Modifier_Base_11_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Modifier_Base"));
  }
  /** Test property {@code Emoji_Presentation}. */
  @Test
  public void emoji_Emoji_Presentation() throws Exception {
    UnicodeEmoji_Emoji_Presentation_11_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Presentation_11_0::new),
            UnicodeEmoji_Emoji_Presentation_11_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Presentation"));
  }
  /** Test property {@code Extended_Pictographic}. */
  @Test
  public void emoji_Extended_Pictographic() throws Exception {
    UnicodeEmoji_Extended_Pictographic_11_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Extended_Pictographic_11_0::new),
            UnicodeEmoji_Extended_Pictographic_11_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Extended_Pictographic"));
  }
  /** Test property {@code Emoji_Component}. */
  @Test
  public void emoji_Emoji_Component() throws Exception {
    UnicodeEmoji_Emoji_Component_11_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Component_11_0::new),
            UnicodeEmoji_Emoji_Component_11_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Component"));
  }
}
