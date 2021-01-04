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
package de.jflex.testcase.unicode.unicode_3_1;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test that parsing all Unicode codepoints detects the correct block ranges for Unicode 3.1. */
public class UnicodeBlocksTest_3_1 {
  @Test
  public void testBlocks() throws Exception {
    ImmutableList<BlockSpec> blocks =
        UnicodeDataScanners.getBlocks(
            ScannerFactory.of(UnicodeBlocks_3_1::new),
            UnicodeBlocks_3_1.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(blocks)
        .containsAllOf(
            BlockSpec.create("Basic Latin", 0x0000, 0x007F),
            BlockSpec.create("Latin-1 Supplement", 0x0080, 0x00FF),
            BlockSpec.create("Latin Extended-A", 0x0100, 0x017F),
            BlockSpec.create("Latin Extended-B", 0x0180, 0x024F),
            BlockSpec.create("IPA Extensions", 0x0250, 0x02AF),
            BlockSpec.create("Spacing Modifier Letters", 0x02B0, 0x02FF),
            BlockSpec.create("Combining Diacritical Marks", 0x0300, 0x036F),
            BlockSpec.create("Greek", 0x0370, 0x03FF),
            BlockSpec.create("Cyrillic", 0x0400, 0x04FF),
            BlockSpec.create("Armenian", 0x0530, 0x058F),
            BlockSpec.create("Hebrew", 0x0590, 0x05FF),
            BlockSpec.create("Arabic", 0x0600, 0x06FF),
            BlockSpec.create("Syriac", 0x0700, 0x074F),
            BlockSpec.create("Thaana", 0x0780, 0x07BF),
            BlockSpec.create("Devanagari", 0x0900, 0x097F),
            BlockSpec.create("Bengali", 0x0980, 0x09FF),
            BlockSpec.create("Gurmukhi", 0x0A00, 0x0A7F),
            BlockSpec.create("Gujarati", 0x0A80, 0x0AFF),
            BlockSpec.create("Oriya", 0x0B00, 0x0B7F),
            BlockSpec.create("Tamil", 0x0B80, 0x0BFF),
            BlockSpec.create("Telugu", 0x0C00, 0x0C7F),
            BlockSpec.create("Kannada", 0x0C80, 0x0CFF),
            BlockSpec.create("Malayalam", 0x0D00, 0x0D7F),
            BlockSpec.create("Sinhala", 0x0D80, 0x0DFF),
            BlockSpec.create("Thai", 0x0E00, 0x0E7F),
            BlockSpec.create("Lao", 0x0E80, 0x0EFF),
            BlockSpec.create("Tibetan", 0x0F00, 0x0FFF),
            BlockSpec.create("Myanmar", 0x1000, 0x109F),
            BlockSpec.create("Georgian", 0x10A0, 0x10FF),
            BlockSpec.create("Hangul Jamo", 0x1100, 0x11FF),
            BlockSpec.create("Ethiopic", 0x1200, 0x137F),
            BlockSpec.create("Cherokee", 0x13A0, 0x13FF),
            BlockSpec.create("Unified Canadian Aboriginal Syllabics", 0x1400, 0x167F),
            BlockSpec.create("Ogham", 0x1680, 0x169F),
            BlockSpec.create("Runic", 0x16A0, 0x16FF),
            BlockSpec.create("Khmer", 0x1780, 0x17FF),
            BlockSpec.create("Mongolian", 0x1800, 0x18AF),
            BlockSpec.create("Latin Extended Additional", 0x1E00, 0x1EFF),
            BlockSpec.create("Greek Extended", 0x1F00, 0x1FFF),
            BlockSpec.create("General Punctuation", 0x2000, 0x206F),
            BlockSpec.create("Superscripts and Subscripts", 0x2070, 0x209F),
            BlockSpec.create("Currency Symbols", 0x20A0, 0x20CF),
            BlockSpec.create("Combining Marks for Symbols", 0x20D0, 0x20FF),
            BlockSpec.create("Letterlike Symbols", 0x2100, 0x214F),
            BlockSpec.create("Number Forms", 0x2150, 0x218F),
            BlockSpec.create("Arrows", 0x2190, 0x21FF),
            BlockSpec.create("Mathematical Operators", 0x2200, 0x22FF),
            BlockSpec.create("Miscellaneous Technical", 0x2300, 0x23FF),
            BlockSpec.create("Control Pictures", 0x2400, 0x243F),
            BlockSpec.create("Optical Character Recognition", 0x2440, 0x245F),
            BlockSpec.create("Enclosed Alphanumerics", 0x2460, 0x24FF),
            BlockSpec.create("Box Drawing", 0x2500, 0x257F),
            BlockSpec.create("Block Elements", 0x2580, 0x259F),
            BlockSpec.create("Geometric Shapes", 0x25A0, 0x25FF),
            BlockSpec.create("Miscellaneous Symbols", 0x2600, 0x26FF),
            BlockSpec.create("Dingbats", 0x2700, 0x27BF),
            BlockSpec.create("Braille Patterns", 0x2800, 0x28FF),
            BlockSpec.create("CJK Radicals Supplement", 0x2E80, 0x2EFF),
            BlockSpec.create("Kangxi Radicals", 0x2F00, 0x2FDF),
            BlockSpec.create("Ideographic Description Characters", 0x2FF0, 0x2FFF),
            BlockSpec.create("CJK Symbols and Punctuation", 0x3000, 0x303F),
            BlockSpec.create("Hiragana", 0x3040, 0x309F),
            BlockSpec.create("Katakana", 0x30A0, 0x30FF),
            BlockSpec.create("Bopomofo", 0x3100, 0x312F),
            BlockSpec.create("Hangul Compatibility Jamo", 0x3130, 0x318F),
            BlockSpec.create("Kanbun", 0x3190, 0x319F),
            BlockSpec.create("Bopomofo Extended", 0x31A0, 0x31BF),
            BlockSpec.create("Enclosed CJK Letters and Months", 0x3200, 0x32FF),
            BlockSpec.create("CJK Compatibility", 0x3300, 0x33FF),
            BlockSpec.create("CJK Unified Ideographs Extension A", 0x3400, 0x4DB5),
            BlockSpec.create("CJK Unified Ideographs", 0x4E00, 0x9FFF),
            BlockSpec.create("Yi Syllables", 0xA000, 0xA48F),
            BlockSpec.create("Yi Radicals", 0xA490, 0xA4CF),
            BlockSpec.create("Hangul Syllables", 0xAC00, 0xD7A3),
            BlockSpec.create("Private Use", 0xE000, 0xF8FF),
            BlockSpec.create("CJK Compatibility Ideographs", 0xF900, 0xFAFF),
            BlockSpec.create("Alphabetic Presentation Forms", 0xFB00, 0xFB4F),
            BlockSpec.create("Arabic Presentation Forms-A", 0xFB50, 0xFDFF),
            BlockSpec.create("Combining Half Marks", 0xFE20, 0xFE2F),
            BlockSpec.create("CJK Compatibility Forms", 0xFE30, 0xFE4F),
            BlockSpec.create("Small Form Variants", 0xFE50, 0xFE6F),
            BlockSpec.create("Arabic Presentation Forms-B", 0xFE70, 0xFEFE),
            BlockSpec.create("Specials", 0xFEFF, 0xFEFF),
            BlockSpec.create("Halfwidth and Fullwidth Forms", 0xFF00, 0xFFEF),
            BlockSpec.create("Specials", 0xFFF0, 0xFFFD),
            BlockSpec.create("Old Italic", 0x10300, 0x1032F),
            BlockSpec.create("Gothic", 0x10330, 0x1034F),
            BlockSpec.create("Deseret", 0x10400, 0x1044F),
            BlockSpec.create("Byzantine Musical Symbols", 0x1D000, 0x1D0FF),
            BlockSpec.create("Musical Symbols", 0x1D100, 0x1D1FF),
            BlockSpec.create("Mathematical Alphanumeric Symbols", 0x1D400, 0x1D7FF),
            BlockSpec.create("CJK Unified Ideographs Extension B", 0x20000, 0x2A6D6),
            BlockSpec.create("CJK Compatibility Ideographs Supplement", 0x2F800, 0x2FA1F),
            BlockSpec.create("Tags", 0xE0000, 0xE007F),
            BlockSpec.create("Private Use", 0xF0000, 0xFFFFD),
            BlockSpec.create("Private Use", 0x100000, 0x10FFFD));
  }
}
