/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
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
package de.jflex.testcase.unicode.unicode_12_0;

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
public class UnicodeEmojiTest_12_0 {

  private static final Path PACKAGE_DIRECTORY =
      Paths.get("javatests/de/jflex/testcase/unicode").resolve("unicode_12_0");

  private static ImmutableList<CodepointRange> expected;

  private static ImmutableList<CodepointRange> readGolden(String propName) throws Exception {
    Path expectedFile = PACKAGE_DIRECTORY.resolve("UnicodeEmoji_" + propName + "_12_0.output");
    return SimpleIntervalsParser.parseRanges(expectedFile);
  }

  /** Test property {@code Emoji}. */
  @Test
  public void emoji_Emoji() throws Exception {
    UnicodeEmoji_Emoji_12_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_12_0::new),
            UnicodeEmoji_Emoji_12_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji"));
  }
  /** Test property {@code Emoji_Modifier}. */
  @Test
  public void emoji_Emoji_Modifier() throws Exception {
    UnicodeEmoji_Emoji_Modifier_12_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Modifier_12_0::new),
            UnicodeEmoji_Emoji_Modifier_12_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Modifier"));
  }
  /** Test property {@code Emoji_Modifier_Base}. */
  @Test
  public void emoji_Emoji_Modifier_Base() throws Exception {
    UnicodeEmoji_Emoji_Modifier_Base_12_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Modifier_Base_12_0::new),
            UnicodeEmoji_Emoji_Modifier_Base_12_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Modifier_Base"));
  }
  /** Test property {@code Emoji_Presentation}. */
  @Test
  public void emoji_Emoji_Presentation() throws Exception {
    UnicodeEmoji_Emoji_Presentation_12_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Presentation_12_0::new),
            UnicodeEmoji_Emoji_Presentation_12_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Presentation"));
  }
  /** Test property {@code Emoji_Component}. */
  @Test
  public void emoji_Emoji_Component() throws Exception {
    UnicodeEmoji_Emoji_Component_12_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeEmoji_Emoji_Component_12_0::new),
            UnicodeEmoji_Emoji_Component_12_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.ranges()).isEqualTo(readGolden("Emoji_Component"));
  }
}
