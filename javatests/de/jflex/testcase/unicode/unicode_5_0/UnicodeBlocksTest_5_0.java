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
package de.jflex.testcase.unicode.unicode_5_0;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import javax.annotation.Generated;
import org.junit.Test;

/** Test that parsing all Unicode codepoints detects the correct block ranges for Unicode 5.0. */
@Generated("de.jflex.migration.unicodedatatest.testblock.BlocksTestGenerator")
public class UnicodeBlocksTest_5_0 {
  @Test
  public void testBlocks() throws Exception {
    UnicodeBlocks_5_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeBlocks_5_0::new),
            UnicodeBlocks_5_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.blocks())
        .containsExactly(
            BlockSpec.create("Basic Latin", 0x0000, 0x007F),
            BlockSpec.create("Latin-1 Supplement", 0x0080, 0x00FF),
            BlockSpec.create("Latin Extended-A", 0x0100, 0x017F),
            BlockSpec.create("Latin Extended-B", 0x0180, 0x024F),
            BlockSpec.create("IPA Extensions", 0x0250, 0x02AF),
            BlockSpec.create("Spacing Modifier Letters", 0x02B0, 0x02FF),
            BlockSpec.create("Combining Diacritical Marks", 0x0300, 0x036F),
            BlockSpec.create("Greek and Coptic", 0x0370, 0x03FF),
            BlockSpec.create("Cyrillic", 0x0400, 0x04FF),
            BlockSpec.create("Cyrillic Supplement", 0x0500, 0x052F),
            BlockSpec.create("Armenian", 0x0530, 0x058F),
            BlockSpec.create("Hebrew", 0x0590, 0x05FF),
            BlockSpec.create("Arabic", 0x0600, 0x06FF),
            BlockSpec.create("Syriac", 0x0700, 0x074F),
            BlockSpec.create("Arabic Supplement", 0x0750, 0x077F),
            BlockSpec.create("Thaana", 0x0780, 0x07BF),
            BlockSpec.create("NKo", 0x07C0, 0x07FF),
            BlockSpec.create("No Block", 0x0800, 0x08FF),
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
            BlockSpec.create("Ethiopic Supplement", 0x1380, 0x139F),
            BlockSpec.create("Cherokee", 0x13A0, 0x13FF),
            BlockSpec.create("Unified Canadian Aboriginal Syllabics", 0x1400, 0x167F),
            BlockSpec.create("Ogham", 0x1680, 0x169F),
            BlockSpec.create("Runic", 0x16A0, 0x16FF),
            BlockSpec.create("Tagalog", 0x1700, 0x171F),
            BlockSpec.create("Hanunoo", 0x1720, 0x173F),
            BlockSpec.create("Buhid", 0x1740, 0x175F),
            BlockSpec.create("Tagbanwa", 0x1760, 0x177F),
            BlockSpec.create("Khmer", 0x1780, 0x17FF),
            BlockSpec.create("Mongolian", 0x1800, 0x18AF),
            BlockSpec.create("No Block", 0x18B0, 0x18FF),
            BlockSpec.create("Limbu", 0x1900, 0x194F),
            BlockSpec.create("Tai Le", 0x1950, 0x197F),
            BlockSpec.create("New Tai Lue", 0x1980, 0x19DF),
            BlockSpec.create("Khmer Symbols", 0x19E0, 0x19FF),
            BlockSpec.create("Buginese", 0x1A00, 0x1A1F),
            BlockSpec.create("No Block", 0x1A20, 0x1AFF),
            BlockSpec.create("Balinese", 0x1B00, 0x1B7F),
            BlockSpec.create("No Block", 0x1B80, 0x1CFF),
            BlockSpec.create("Phonetic Extensions", 0x1D00, 0x1D7F),
            BlockSpec.create("Phonetic Extensions Supplement", 0x1D80, 0x1DBF),
            BlockSpec.create("Combining Diacritical Marks Supplement", 0x1DC0, 0x1DFF),
            BlockSpec.create("Latin Extended Additional", 0x1E00, 0x1EFF),
            BlockSpec.create("Greek Extended", 0x1F00, 0x1FFF),
            BlockSpec.create("General Punctuation", 0x2000, 0x206F),
            BlockSpec.create("Superscripts and Subscripts", 0x2070, 0x209F),
            BlockSpec.create("Currency Symbols", 0x20A0, 0x20CF),
            BlockSpec.create("Combining Diacritical Marks for Symbols", 0x20D0, 0x20FF),
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
            BlockSpec.create("Miscellaneous Mathematical Symbols-A", 0x27C0, 0x27EF),
            BlockSpec.create("Supplemental Arrows-A", 0x27F0, 0x27FF),
            BlockSpec.create("Braille Patterns", 0x2800, 0x28FF),
            BlockSpec.create("Supplemental Arrows-B", 0x2900, 0x297F),
            BlockSpec.create("Miscellaneous Mathematical Symbols-B", 0x2980, 0x29FF),
            BlockSpec.create("Supplemental Mathematical Operators", 0x2A00, 0x2AFF),
            BlockSpec.create("Miscellaneous Symbols and Arrows", 0x2B00, 0x2BFF),
            BlockSpec.create("Glagolitic", 0x2C00, 0x2C5F),
            BlockSpec.create("Latin Extended-C", 0x2C60, 0x2C7F),
            BlockSpec.create("Coptic", 0x2C80, 0x2CFF),
            BlockSpec.create("Georgian Supplement", 0x2D00, 0x2D2F),
            BlockSpec.create("Tifinagh", 0x2D30, 0x2D7F),
            BlockSpec.create("Ethiopic Extended", 0x2D80, 0x2DDF),
            BlockSpec.create("No Block", 0x2DE0, 0x2DFF),
            BlockSpec.create("Supplemental Punctuation", 0x2E00, 0x2E7F),
            BlockSpec.create("CJK Radicals Supplement", 0x2E80, 0x2EFF),
            BlockSpec.create("Kangxi Radicals", 0x2F00, 0x2FDF),
            BlockSpec.create("No Block", 0x2FE0, 0x2FEF),
            BlockSpec.create("Ideographic Description Characters", 0x2FF0, 0x2FFF),
            BlockSpec.create("CJK Symbols and Punctuation", 0x3000, 0x303F),
            BlockSpec.create("Hiragana", 0x3040, 0x309F),
            BlockSpec.create("Katakana", 0x30A0, 0x30FF),
            BlockSpec.create("Bopomofo", 0x3100, 0x312F),
            BlockSpec.create("Hangul Compatibility Jamo", 0x3130, 0x318F),
            BlockSpec.create("Kanbun", 0x3190, 0x319F),
            BlockSpec.create("Bopomofo Extended", 0x31A0, 0x31BF),
            BlockSpec.create("CJK Strokes", 0x31C0, 0x31EF),
            BlockSpec.create("Katakana Phonetic Extensions", 0x31F0, 0x31FF),
            BlockSpec.create("Enclosed CJK Letters and Months", 0x3200, 0x32FF),
            BlockSpec.create("CJK Compatibility", 0x3300, 0x33FF),
            BlockSpec.create("CJK Unified Ideographs Extension A", 0x3400, 0x4DBF),
            BlockSpec.create("Yijing Hexagram Symbols", 0x4DC0, 0x4DFF),
            BlockSpec.create("CJK Unified Ideographs", 0x4E00, 0x9FFF),
            BlockSpec.create("Yi Syllables", 0xA000, 0xA48F),
            BlockSpec.create("Yi Radicals", 0xA490, 0xA4CF),
            BlockSpec.create("No Block", 0xA4D0, 0xA6FF),
            BlockSpec.create("Modifier Tone Letters", 0xA700, 0xA71F),
            BlockSpec.create("Latin Extended-D", 0xA720, 0xA7FF),
            BlockSpec.create("Syloti Nagri", 0xA800, 0xA82F),
            BlockSpec.create("No Block", 0xA830, 0xA83F),
            BlockSpec.create("Phags-pa", 0xA840, 0xA87F),
            BlockSpec.create("No Block", 0xA880, 0xABFF),
            BlockSpec.create("Hangul Syllables", 0xAC00, 0xD7AF),
            BlockSpec.create("No Block", 0xD7B0, 0xD7FF),
            BlockSpec.create("Private Use Area", 0xE000, 0xF8FF),
            BlockSpec.create("CJK Compatibility Ideographs", 0xF900, 0xFAFF),
            BlockSpec.create("Alphabetic Presentation Forms", 0xFB00, 0xFB4F),
            BlockSpec.create("Arabic Presentation Forms-A", 0xFB50, 0xFDFF),
            BlockSpec.create("Variation Selectors", 0xFE00, 0xFE0F),
            BlockSpec.create("Vertical Forms", 0xFE10, 0xFE1F),
            BlockSpec.create("Combining Half Marks", 0xFE20, 0xFE2F),
            BlockSpec.create("CJK Compatibility Forms", 0xFE30, 0xFE4F),
            BlockSpec.create("Small Form Variants", 0xFE50, 0xFE6F),
            BlockSpec.create("Arabic Presentation Forms-B", 0xFE70, 0xFEFF),
            BlockSpec.create("Halfwidth and Fullwidth Forms", 0xFF00, 0xFFEF),
            BlockSpec.create("Specials", 0xFFF0, 0xFFFF),
            BlockSpec.create("Linear B Syllabary", 0x10000, 0x1007F),
            BlockSpec.create("Linear B Ideograms", 0x10080, 0x100FF),
            BlockSpec.create("Aegean Numbers", 0x10100, 0x1013F),
            BlockSpec.create("Ancient Greek Numbers", 0x10140, 0x1018F),
            BlockSpec.create("No Block", 0x10190, 0x102FF),
            BlockSpec.create("Old Italic", 0x10300, 0x1032F),
            BlockSpec.create("Gothic", 0x10330, 0x1034F),
            BlockSpec.create("No Block", 0x10350, 0x1037F),
            BlockSpec.create("Ugaritic", 0x10380, 0x1039F),
            BlockSpec.create("Old Persian", 0x103A0, 0x103DF),
            BlockSpec.create("No Block", 0x103E0, 0x103FF),
            BlockSpec.create("Deseret", 0x10400, 0x1044F),
            BlockSpec.create("Shavian", 0x10450, 0x1047F),
            BlockSpec.create("Osmanya", 0x10480, 0x104AF),
            BlockSpec.create("No Block", 0x104B0, 0x107FF),
            BlockSpec.create("Cypriot Syllabary", 0x10800, 0x1083F),
            BlockSpec.create("No Block", 0x10840, 0x108FF),
            BlockSpec.create("Phoenician", 0x10900, 0x1091F),
            BlockSpec.create("No Block", 0x10920, 0x109FF),
            BlockSpec.create("Kharoshthi", 0x10A00, 0x10A5F),
            BlockSpec.create("No Block", 0x10A60, 0x11FFF),
            BlockSpec.create("Cuneiform", 0x12000, 0x123FF),
            BlockSpec.create("Cuneiform Numbers and Punctuation", 0x12400, 0x1247F),
            BlockSpec.create("No Block", 0x12480, 0x1CFFF),
            BlockSpec.create("Byzantine Musical Symbols", 0x1D000, 0x1D0FF),
            BlockSpec.create("Musical Symbols", 0x1D100, 0x1D1FF),
            BlockSpec.create("Ancient Greek Musical Notation", 0x1D200, 0x1D24F),
            BlockSpec.create("No Block", 0x1D250, 0x1D2FF),
            BlockSpec.create("Tai Xuan Jing Symbols", 0x1D300, 0x1D35F),
            BlockSpec.create("Counting Rod Numerals", 0x1D360, 0x1D37F),
            BlockSpec.create("No Block", 0x1D380, 0x1D3FF),
            BlockSpec.create("Mathematical Alphanumeric Symbols", 0x1D400, 0x1D7FF),
            BlockSpec.create("No Block", 0x1D800, 0x1FFFF),
            BlockSpec.create("CJK Unified Ideographs Extension B", 0x20000, 0x2A6DF),
            BlockSpec.create("No Block", 0x2A6E0, 0x2F7FF),
            BlockSpec.create("CJK Compatibility Ideographs Supplement", 0x2F800, 0x2FA1F),
            BlockSpec.create("No Block", 0x2FA20, 0xDFFFF),
            BlockSpec.create("Tags", 0xE0000, 0xE007F),
            BlockSpec.create("No Block", 0xE0080, 0xE00FF),
            BlockSpec.create("Variation Selectors Supplement", 0xE0100, 0xE01EF),
            BlockSpec.create("No Block", 0xE01F0, 0xEFFFF),
            BlockSpec.create("Supplementary Private Use Area-A", 0xF0000, 0xFFFFF),
            BlockSpec.create("Supplementary Private Use Area-B", 0x100000, 0x10FFFF));
  }
}
