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
package de.jflex.testcase.unicode.unicode_3_0;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.util.scanner.ScannerFactory;
import javax.annotation.Generated;
import org.junit.Test;

/** Test that parsing all Unicode codepoints detects the correct block ranges for Unicode 3.0. */
@Generated("de.jflex.migration.unicodedatatest.testblock.BlocksTestGenerator")
public class UnicodeBlocksTest_3_0 {
  @Test
  public void testBlocks() throws Exception {
    UnicodeBlocks_3_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeBlocks_3_0::new),
            UnicodeBlocks_3_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.blocks())
        .containsExactly(
            NamedCodepointRange.create("Basic Latin", 0x0000, 0x007F),
            NamedCodepointRange.create("Latin-1 Supplement", 0x0080, 0x00FF),
            NamedCodepointRange.create("Latin Extended-A", 0x0100, 0x017F),
            NamedCodepointRange.create("Latin Extended-B", 0x0180, 0x024F),
            NamedCodepointRange.create("IPA Extensions", 0x0250, 0x02AF),
            NamedCodepointRange.create("Spacing Modifier Letters", 0x02B0, 0x02FF),
            NamedCodepointRange.create("Combining Diacritical Marks", 0x0300, 0x036F),
            NamedCodepointRange.create("Greek", 0x0370, 0x03FF),
            NamedCodepointRange.create("Cyrillic", 0x0400, 0x04FF),
            NamedCodepointRange.create("No Block", 0x0500, 0x052F),
            NamedCodepointRange.create("Armenian", 0x0530, 0x058F),
            NamedCodepointRange.create("Hebrew", 0x0590, 0x05FF),
            NamedCodepointRange.create("Arabic", 0x0600, 0x06FF),
            NamedCodepointRange.create("Syriac  ", 0x0700, 0x074F),
            NamedCodepointRange.create("No Block", 0x0750, 0x077F),
            NamedCodepointRange.create("Thaana", 0x0780, 0x07BF),
            NamedCodepointRange.create("No Block", 0x07C0, 0x08FF),
            NamedCodepointRange.create("Devanagari", 0x0900, 0x097F),
            NamedCodepointRange.create("Bengali", 0x0980, 0x09FF),
            NamedCodepointRange.create("Gurmukhi", 0x0A00, 0x0A7F),
            NamedCodepointRange.create("Gujarati", 0x0A80, 0x0AFF),
            NamedCodepointRange.create("Oriya", 0x0B00, 0x0B7F),
            NamedCodepointRange.create("Tamil", 0x0B80, 0x0BFF),
            NamedCodepointRange.create("Telugu", 0x0C00, 0x0C7F),
            NamedCodepointRange.create("Kannada", 0x0C80, 0x0CFF),
            NamedCodepointRange.create("Malayalam", 0x0D00, 0x0D7F),
            NamedCodepointRange.create("Sinhala", 0x0D80, 0x0DFF),
            NamedCodepointRange.create("Thai", 0x0E00, 0x0E7F),
            NamedCodepointRange.create("Lao", 0x0E80, 0x0EFF),
            NamedCodepointRange.create("Tibetan", 0x0F00, 0x0FFF),
            NamedCodepointRange.create("Myanmar ", 0x1000, 0x109F),
            NamedCodepointRange.create("Georgian", 0x10A0, 0x10FF),
            NamedCodepointRange.create("Hangul Jamo", 0x1100, 0x11FF),
            NamedCodepointRange.create("Ethiopic", 0x1200, 0x137F),
            NamedCodepointRange.create("No Block", 0x1380, 0x139F),
            NamedCodepointRange.create("Cherokee", 0x13A0, 0x13FF),
            NamedCodepointRange.create("Unified Canadian Aboriginal Syllabics", 0x1400, 0x167F),
            NamedCodepointRange.create("Ogham", 0x1680, 0x169F),
            NamedCodepointRange.create("Runic", 0x16A0, 0x16FF),
            NamedCodepointRange.create("No Block", 0x1700, 0x177F),
            NamedCodepointRange.create("Khmer", 0x1780, 0x17FF),
            NamedCodepointRange.create("Mongolian", 0x1800, 0x18AF),
            NamedCodepointRange.create("No Block", 0x18B0, 0x1DFF),
            NamedCodepointRange.create("Latin Extended Additional", 0x1E00, 0x1EFF),
            NamedCodepointRange.create("Greek Extended", 0x1F00, 0x1FFF),
            NamedCodepointRange.create("General Punctuation", 0x2000, 0x206F),
            NamedCodepointRange.create("Superscripts and Subscripts", 0x2070, 0x209F),
            NamedCodepointRange.create("Currency Symbols", 0x20A0, 0x20CF),
            NamedCodepointRange.create("Combining Marks for Symbols", 0x20D0, 0x20FF),
            NamedCodepointRange.create("Letterlike Symbols", 0x2100, 0x214F),
            NamedCodepointRange.create("Number Forms", 0x2150, 0x218F),
            NamedCodepointRange.create("Arrows", 0x2190, 0x21FF),
            NamedCodepointRange.create("Mathematical Operators", 0x2200, 0x22FF),
            NamedCodepointRange.create("Miscellaneous Technical", 0x2300, 0x23FF),
            NamedCodepointRange.create("Control Pictures", 0x2400, 0x243F),
            NamedCodepointRange.create("Optical Character Recognition", 0x2440, 0x245F),
            NamedCodepointRange.create("Enclosed Alphanumerics", 0x2460, 0x24FF),
            NamedCodepointRange.create("Box Drawing", 0x2500, 0x257F),
            NamedCodepointRange.create("Block Elements", 0x2580, 0x259F),
            NamedCodepointRange.create("Geometric Shapes", 0x25A0, 0x25FF),
            NamedCodepointRange.create("Miscellaneous Symbols", 0x2600, 0x26FF),
            NamedCodepointRange.create("Dingbats", 0x2700, 0x27BF),
            NamedCodepointRange.create("No Block", 0x27C0, 0x27FF),
            NamedCodepointRange.create("Braille Patterns", 0x2800, 0x28FF),
            NamedCodepointRange.create("No Block", 0x2900, 0x2E7F),
            NamedCodepointRange.create("CJK Radicals Supplement", 0x2E80, 0x2EFF),
            NamedCodepointRange.create("Kangxi Radicals", 0x2F00, 0x2FDF),
            NamedCodepointRange.create("No Block", 0x2FE0, 0x2FEF),
            NamedCodepointRange.create("Ideographic Description Characters", 0x2FF0, 0x2FFF),
            NamedCodepointRange.create("CJK Symbols and Punctuation", 0x3000, 0x303F),
            NamedCodepointRange.create("Hiragana", 0x3040, 0x309F),
            NamedCodepointRange.create("Katakana", 0x30A0, 0x30FF),
            NamedCodepointRange.create("Bopomofo", 0x3100, 0x312F),
            NamedCodepointRange.create("Hangul Compatibility Jamo", 0x3130, 0x318F),
            NamedCodepointRange.create("Kanbun", 0x3190, 0x319F),
            NamedCodepointRange.create("Bopomofo Extended", 0x31A0, 0x31BF),
            NamedCodepointRange.create("No Block", 0x31C0, 0x31FF),
            NamedCodepointRange.create("Enclosed CJK Letters and Months", 0x3200, 0x32FF),
            NamedCodepointRange.create("CJK Compatibility", 0x3300, 0x33FF),
            NamedCodepointRange.create("CJK Unified Ideographs Extension A", 0x3400, 0x4DB5),
            NamedCodepointRange.create("No Block", 0x4DB6, 0x4DFF),
            NamedCodepointRange.create("CJK Unified Ideographs", 0x4E00, 0x9FFF),
            NamedCodepointRange.create("Yi Syllables", 0xA000, 0xA48F),
            NamedCodepointRange.create("Yi Radicals", 0xA490, 0xA4CF),
            NamedCodepointRange.create("No Block", 0xA4D0, 0xABFF),
            NamedCodepointRange.create("Hangul Syllables", 0xAC00, 0xD7A3),
            NamedCodepointRange.create("No Block", 0xD7A4, 0xD7FF),
            NamedCodepointRange.create("Private Use", 0xE000, 0xF8FF),
            NamedCodepointRange.create("CJK Compatibility Ideographs", 0xF900, 0xFAFF),
            NamedCodepointRange.create("Alphabetic Presentation Forms", 0xFB00, 0xFB4F),
            NamedCodepointRange.create("Arabic Presentation Forms-A", 0xFB50, 0xFDFF),
            NamedCodepointRange.create("No Block", 0xFE00, 0xFE1F),
            NamedCodepointRange.create("Combining Half Marks", 0xFE20, 0xFE2F),
            NamedCodepointRange.create("CJK Compatibility Forms", 0xFE30, 0xFE4F),
            NamedCodepointRange.create("Small Form Variants", 0xFE50, 0xFE6F),
            NamedCodepointRange.create("Arabic Presentation Forms-B", 0xFE70, 0xFEFE),
            NamedCodepointRange.create("Specials", 0xFEFF, 0xFEFF),
            NamedCodepointRange.create("Halfwidth and Fullwidth Forms", 0xFF00, 0xFFEF),
            NamedCodepointRange.create("Specials", 0xFFF0, 0xFFFD),
            NamedCodepointRange.create("No Block", 0xFFFE, 0x10FFFF));
  }
}
