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
package de.jflex.testcase.unicode.unicode_9_0;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test that parsing all Unicode codepoints detects the correct block ranges for Unicode 9.0. */
public class UnicodeBlocksTest_9_0 {
  @Test
  public void testBlocks() throws Exception {
    ImmutableList<BlockSpec> blocks =
        UnicodeDataScanners.getBlocks(
            ScannerFactory.of(UnicodeBlocks_9_0::new),
            UnicodeBlocks_9_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(blocks)
        .containsAllOf(
            // 0000..007F; Basic Latin
            BlockSpec.create("Basic Latin", 0x0000, 0x007F)
            // 0080..00FF; Latin-1 Supplement
            ,
            BlockSpec.create("Latin-1 Supplement", 0x0080, 0x00FF)
            // 0100..017F; Latin Extended-A
            ,
            BlockSpec.create("Latin Extended-A", 0x0100, 0x017F)
            // 0180..024F; Latin Extended-B
            ,
            BlockSpec.create("Latin Extended-B", 0x0180, 0x024F)
            // 0250..02AF; IPA Extensions
            ,
            BlockSpec.create("IPA Extensions", 0x0250, 0x02AF)
            // 02B0..02FF; Spacing Modifier Letters
            ,
            BlockSpec.create("Spacing Modifier Letters", 0x02B0, 0x02FF)
            // 0300..036F; Combining Diacritical Marks
            ,
            BlockSpec.create("Combining Diacritical Marks", 0x0300, 0x036F)
            // 0370..03FF; Greek and Coptic
            ,
            BlockSpec.create("Greek and Coptic", 0x0370, 0x03FF)
            // 0400..04FF; Cyrillic
            ,
            BlockSpec.create("Cyrillic", 0x0400, 0x04FF)
            // 0500..052F; Cyrillic Supplement
            ,
            BlockSpec.create("Cyrillic Supplement", 0x0500, 0x052F)
            // 0530..058F; Armenian
            ,
            BlockSpec.create("Armenian", 0x0530, 0x058F)
            // 0590..05FF; Hebrew
            ,
            BlockSpec.create("Hebrew", 0x0590, 0x05FF)
            // 0600..06FF; Arabic
            ,
            BlockSpec.create("Arabic", 0x0600, 0x06FF)
            // 0700..074F; Syriac
            ,
            BlockSpec.create("Syriac", 0x0700, 0x074F)
            // 0750..077F; Arabic Supplement
            ,
            BlockSpec.create("Arabic Supplement", 0x0750, 0x077F)
            // 0780..07BF; Thaana
            ,
            BlockSpec.create("Thaana", 0x0780, 0x07BF)
            // 07C0..07FF; NKo
            ,
            BlockSpec.create("NKo", 0x07C0, 0x07FF)
            // 0800..083F; Samaritan
            ,
            BlockSpec.create("Samaritan", 0x0800, 0x083F)
            // 0840..085F; Mandaic
            ,
            BlockSpec.create("Mandaic", 0x0840, 0x085F)
            // 08A0..08FF; Arabic Extended-A
            ,
            BlockSpec.create("Arabic Extended-A", 0x08A0, 0x08FF)
            // 0900..097F; Devanagari
            ,
            BlockSpec.create("Devanagari", 0x0900, 0x097F)
            // 0980..09FF; Bengali
            ,
            BlockSpec.create("Bengali", 0x0980, 0x09FF)
            // 0A00..0A7F; Gurmukhi
            ,
            BlockSpec.create("Gurmukhi", 0x0A00, 0x0A7F)
            // 0A80..0AFF; Gujarati
            ,
            BlockSpec.create("Gujarati", 0x0A80, 0x0AFF)
            // 0B00..0B7F; Oriya
            ,
            BlockSpec.create("Oriya", 0x0B00, 0x0B7F)
            // 0B80..0BFF; Tamil
            ,
            BlockSpec.create("Tamil", 0x0B80, 0x0BFF)
            // 0C00..0C7F; Telugu
            ,
            BlockSpec.create("Telugu", 0x0C00, 0x0C7F)
            // 0C80..0CFF; Kannada
            ,
            BlockSpec.create("Kannada", 0x0C80, 0x0CFF)
            // 0D00..0D7F; Malayalam
            ,
            BlockSpec.create("Malayalam", 0x0D00, 0x0D7F)
            // 0D80..0DFF; Sinhala
            ,
            BlockSpec.create("Sinhala", 0x0D80, 0x0DFF)
            // 0E00..0E7F; Thai
            ,
            BlockSpec.create("Thai", 0x0E00, 0x0E7F)
            // 0E80..0EFF; Lao
            ,
            BlockSpec.create("Lao", 0x0E80, 0x0EFF)
            // 0F00..0FFF; Tibetan
            ,
            BlockSpec.create("Tibetan", 0x0F00, 0x0FFF)
            // 1000..109F; Myanmar
            ,
            BlockSpec.create("Myanmar", 0x1000, 0x109F)
            // 10A0..10FF; Georgian
            ,
            BlockSpec.create("Georgian", 0x10A0, 0x10FF)
            // 1100..11FF; Hangul Jamo
            ,
            BlockSpec.create("Hangul Jamo", 0x1100, 0x11FF)
            // 1200..137F; Ethiopic
            ,
            BlockSpec.create("Ethiopic", 0x1200, 0x137F)
            // 1380..139F; Ethiopic Supplement
            ,
            BlockSpec.create("Ethiopic Supplement", 0x1380, 0x139F)
            // 13A0..13FF; Cherokee
            ,
            BlockSpec.create("Cherokee", 0x13A0, 0x13FF)
            // 1400..167F; Unified Canadian Aboriginal Syllabics
            ,
            BlockSpec.create("Unified Canadian Aboriginal Syllabics", 0x1400, 0x167F)
            // 1680..169F; Ogham
            ,
            BlockSpec.create("Ogham", 0x1680, 0x169F)
            // 16A0..16FF; Runic
            ,
            BlockSpec.create("Runic", 0x16A0, 0x16FF)
            // 1700..171F; Tagalog
            ,
            BlockSpec.create("Tagalog", 0x1700, 0x171F)
            // 1720..173F; Hanunoo
            ,
            BlockSpec.create("Hanunoo", 0x1720, 0x173F)
            // 1740..175F; Buhid
            ,
            BlockSpec.create("Buhid", 0x1740, 0x175F)
            // 1760..177F; Tagbanwa
            ,
            BlockSpec.create("Tagbanwa", 0x1760, 0x177F)
            // 1780..17FF; Khmer
            ,
            BlockSpec.create("Khmer", 0x1780, 0x17FF)
            // 1800..18AF; Mongolian
            ,
            BlockSpec.create("Mongolian", 0x1800, 0x18AF)
            // 18B0..18FF; Unified Canadian Aboriginal Syllabics Extended
            ,
            BlockSpec.create("Unified Canadian Aboriginal Syllabics Extended", 0x18B0, 0x18FF)
            // 1900..194F; Limbu
            ,
            BlockSpec.create("Limbu", 0x1900, 0x194F)
            // 1950..197F; Tai Le
            ,
            BlockSpec.create("Tai Le", 0x1950, 0x197F)
            // 1980..19DF; New Tai Lue
            ,
            BlockSpec.create("New Tai Lue", 0x1980, 0x19DF)
            // 19E0..19FF; Khmer Symbols
            ,
            BlockSpec.create("Khmer Symbols", 0x19E0, 0x19FF)
            // 1A00..1A1F; Buginese
            ,
            BlockSpec.create("Buginese", 0x1A00, 0x1A1F)
            // 1A20..1AAF; Tai Tham
            ,
            BlockSpec.create("Tai Tham", 0x1A20, 0x1AAF)
            // 1AB0..1AFF; Combining Diacritical Marks Extended
            ,
            BlockSpec.create("Combining Diacritical Marks Extended", 0x1AB0, 0x1AFF)
            // 1B00..1B7F; Balinese
            ,
            BlockSpec.create("Balinese", 0x1B00, 0x1B7F)
            // 1B80..1BBF; Sundanese
            ,
            BlockSpec.create("Sundanese", 0x1B80, 0x1BBF)
            // 1BC0..1BFF; Batak
            ,
            BlockSpec.create("Batak", 0x1BC0, 0x1BFF)
            // 1C00..1C4F; Lepcha
            ,
            BlockSpec.create("Lepcha", 0x1C00, 0x1C4F)
            // 1C50..1C7F; Ol Chiki
            ,
            BlockSpec.create("Ol Chiki", 0x1C50, 0x1C7F)
            // 1C80..1C8F; Cyrillic Extended-C
            ,
            BlockSpec.create("Cyrillic Extended-C", 0x1C80, 0x1C8F)
            // 1CC0..1CCF; Sundanese Supplement
            ,
            BlockSpec.create("Sundanese Supplement", 0x1CC0, 0x1CCF)
            // 1CD0..1CFF; Vedic Extensions
            ,
            BlockSpec.create("Vedic Extensions", 0x1CD0, 0x1CFF)
            // 1D00..1D7F; Phonetic Extensions
            ,
            BlockSpec.create("Phonetic Extensions", 0x1D00, 0x1D7F)
            // 1D80..1DBF; Phonetic Extensions Supplement
            ,
            BlockSpec.create("Phonetic Extensions Supplement", 0x1D80, 0x1DBF)
            // 1DC0..1DFF; Combining Diacritical Marks Supplement
            ,
            BlockSpec.create("Combining Diacritical Marks Supplement", 0x1DC0, 0x1DFF)
            // 1E00..1EFF; Latin Extended Additional
            ,
            BlockSpec.create("Latin Extended Additional", 0x1E00, 0x1EFF)
            // 1F00..1FFF; Greek Extended
            ,
            BlockSpec.create("Greek Extended", 0x1F00, 0x1FFF)
            // 2000..206F; General Punctuation
            ,
            BlockSpec.create("General Punctuation", 0x2000, 0x206F)
            // 2070..209F; Superscripts and Subscripts
            ,
            BlockSpec.create("Superscripts and Subscripts", 0x2070, 0x209F)
            // 20A0..20CF; Currency Symbols
            ,
            BlockSpec.create("Currency Symbols", 0x20A0, 0x20CF)
            // 20D0..20FF; Combining Diacritical Marks for Symbols
            ,
            BlockSpec.create("Combining Diacritical Marks for Symbols", 0x20D0, 0x20FF)
            // 2100..214F; Letterlike Symbols
            ,
            BlockSpec.create("Letterlike Symbols", 0x2100, 0x214F)
            // 2150..218F; Number Forms
            ,
            BlockSpec.create("Number Forms", 0x2150, 0x218F)
            // 2190..21FF; Arrows
            ,
            BlockSpec.create("Arrows", 0x2190, 0x21FF)
            // 2200..22FF; Mathematical Operators
            ,
            BlockSpec.create("Mathematical Operators", 0x2200, 0x22FF)
            // 2300..23FF; Miscellaneous Technical
            ,
            BlockSpec.create("Miscellaneous Technical", 0x2300, 0x23FF)
            // 2400..243F; Control Pictures
            ,
            BlockSpec.create("Control Pictures", 0x2400, 0x243F)
            // 2440..245F; Optical Character Recognition
            ,
            BlockSpec.create("Optical Character Recognition", 0x2440, 0x245F)
            // 2460..24FF; Enclosed Alphanumerics
            ,
            BlockSpec.create("Enclosed Alphanumerics", 0x2460, 0x24FF)
            // 2500..257F; Box Drawing
            ,
            BlockSpec.create("Box Drawing", 0x2500, 0x257F)
            // 2580..259F; Block Elements
            ,
            BlockSpec.create("Block Elements", 0x2580, 0x259F)
            // 25A0..25FF; Geometric Shapes
            ,
            BlockSpec.create("Geometric Shapes", 0x25A0, 0x25FF)
            // 2600..26FF; Miscellaneous Symbols
            ,
            BlockSpec.create("Miscellaneous Symbols", 0x2600, 0x26FF)
            // 2700..27BF; Dingbats
            ,
            BlockSpec.create("Dingbats", 0x2700, 0x27BF)
            // 27C0..27EF; Miscellaneous Mathematical Symbols-A
            ,
            BlockSpec.create("Miscellaneous Mathematical Symbols-A", 0x27C0, 0x27EF)
            // 27F0..27FF; Supplemental Arrows-A
            ,
            BlockSpec.create("Supplemental Arrows-A", 0x27F0, 0x27FF)
            // 2800..28FF; Braille Patterns
            ,
            BlockSpec.create("Braille Patterns", 0x2800, 0x28FF)
            // 2900..297F; Supplemental Arrows-B
            ,
            BlockSpec.create("Supplemental Arrows-B", 0x2900, 0x297F)
            // 2980..29FF; Miscellaneous Mathematical Symbols-B
            ,
            BlockSpec.create("Miscellaneous Mathematical Symbols-B", 0x2980, 0x29FF)
            // 2A00..2AFF; Supplemental Mathematical Operators
            ,
            BlockSpec.create("Supplemental Mathematical Operators", 0x2A00, 0x2AFF)
            // 2B00..2BFF; Miscellaneous Symbols and Arrows
            ,
            BlockSpec.create("Miscellaneous Symbols and Arrows", 0x2B00, 0x2BFF)
            // 2C00..2C5F; Glagolitic
            ,
            BlockSpec.create("Glagolitic", 0x2C00, 0x2C5F)
            // 2C60..2C7F; Latin Extended-C
            ,
            BlockSpec.create("Latin Extended-C", 0x2C60, 0x2C7F)
            // 2C80..2CFF; Coptic
            ,
            BlockSpec.create("Coptic", 0x2C80, 0x2CFF)
            // 2D00..2D2F; Georgian Supplement
            ,
            BlockSpec.create("Georgian Supplement", 0x2D00, 0x2D2F)
            // 2D30..2D7F; Tifinagh
            ,
            BlockSpec.create("Tifinagh", 0x2D30, 0x2D7F)
            // 2D80..2DDF; Ethiopic Extended
            ,
            BlockSpec.create("Ethiopic Extended", 0x2D80, 0x2DDF)
            // 2DE0..2DFF; Cyrillic Extended-A
            ,
            BlockSpec.create("Cyrillic Extended-A", 0x2DE0, 0x2DFF)
            // 2E00..2E7F; Supplemental Punctuation
            ,
            BlockSpec.create("Supplemental Punctuation", 0x2E00, 0x2E7F)
            // 2E80..2EFF; CJK Radicals Supplement
            ,
            BlockSpec.create("CJK Radicals Supplement", 0x2E80, 0x2EFF)
            // 2F00..2FDF; Kangxi Radicals
            ,
            BlockSpec.create("Kangxi Radicals", 0x2F00, 0x2FDF)
            // 2FF0..2FFF; Ideographic Description Characters
            ,
            BlockSpec.create("Ideographic Description Characters", 0x2FF0, 0x2FFF)
            // 3000..303F; CJK Symbols and Punctuation
            ,
            BlockSpec.create("CJK Symbols and Punctuation", 0x3000, 0x303F)
            // 3040..309F; Hiragana
            ,
            BlockSpec.create("Hiragana", 0x3040, 0x309F)
            // 30A0..30FF; Katakana
            ,
            BlockSpec.create("Katakana", 0x30A0, 0x30FF)
            // 3100..312F; Bopomofo
            ,
            BlockSpec.create("Bopomofo", 0x3100, 0x312F)
            // 3130..318F; Hangul Compatibility Jamo
            ,
            BlockSpec.create("Hangul Compatibility Jamo", 0x3130, 0x318F)
            // 3190..319F; Kanbun
            ,
            BlockSpec.create("Kanbun", 0x3190, 0x319F)
            // 31A0..31BF; Bopomofo Extended
            ,
            BlockSpec.create("Bopomofo Extended", 0x31A0, 0x31BF)
            // 31C0..31EF; CJK Strokes
            ,
            BlockSpec.create("CJK Strokes", 0x31C0, 0x31EF)
            // 31F0..31FF; Katakana Phonetic Extensions
            ,
            BlockSpec.create("Katakana Phonetic Extensions", 0x31F0, 0x31FF)
            // 3200..32FF; Enclosed CJK Letters and Months
            ,
            BlockSpec.create("Enclosed CJK Letters and Months", 0x3200, 0x32FF)
            // 3300..33FF; CJK Compatibility
            ,
            BlockSpec.create("CJK Compatibility", 0x3300, 0x33FF)
            // 3400..4DBF; CJK Unified Ideographs Extension A
            ,
            BlockSpec.create("CJK Unified Ideographs Extension A", 0x3400, 0x4DBF)
            // 4DC0..4DFF; Yijing Hexagram Symbols
            ,
            BlockSpec.create("Yijing Hexagram Symbols", 0x4DC0, 0x4DFF)
            // 4E00..9FFF; CJK Unified Ideographs
            ,
            BlockSpec.create("CJK Unified Ideographs", 0x4E00, 0x9FFF)
            // A000..A48F; Yi Syllables
            ,
            BlockSpec.create("Yi Syllables", 0xA000, 0xA48F)
            // A490..A4CF; Yi Radicals
            ,
            BlockSpec.create("Yi Radicals", 0xA490, 0xA4CF)
            // A4D0..A4FF; Lisu
            ,
            BlockSpec.create("Lisu", 0xA4D0, 0xA4FF)
            // A500..A63F; Vai
            ,
            BlockSpec.create("Vai", 0xA500, 0xA63F)
            // A640..A69F; Cyrillic Extended-B
            ,
            BlockSpec.create("Cyrillic Extended-B", 0xA640, 0xA69F)
            // A6A0..A6FF; Bamum
            ,
            BlockSpec.create("Bamum", 0xA6A0, 0xA6FF)
            // A700..A71F; Modifier Tone Letters
            ,
            BlockSpec.create("Modifier Tone Letters", 0xA700, 0xA71F)
            // A720..A7FF; Latin Extended-D
            ,
            BlockSpec.create("Latin Extended-D", 0xA720, 0xA7FF)
            // A800..A82F; Syloti Nagri
            ,
            BlockSpec.create("Syloti Nagri", 0xA800, 0xA82F)
            // A830..A83F; Common Indic Number Forms
            ,
            BlockSpec.create("Common Indic Number Forms", 0xA830, 0xA83F)
            // A840..A87F; Phags-pa
            ,
            BlockSpec.create("Phags-pa", 0xA840, 0xA87F)
            // A880..A8DF; Saurashtra
            ,
            BlockSpec.create("Saurashtra", 0xA880, 0xA8DF)
            // A8E0..A8FF; Devanagari Extended
            ,
            BlockSpec.create("Devanagari Extended", 0xA8E0, 0xA8FF)
            // A900..A92F; Kayah Li
            ,
            BlockSpec.create("Kayah Li", 0xA900, 0xA92F)
            // A930..A95F; Rejang
            ,
            BlockSpec.create("Rejang", 0xA930, 0xA95F)
            // A960..A97F; Hangul Jamo Extended-A
            ,
            BlockSpec.create("Hangul Jamo Extended-A", 0xA960, 0xA97F)
            // A980..A9DF; Javanese
            ,
            BlockSpec.create("Javanese", 0xA980, 0xA9DF)
            // A9E0..A9FF; Myanmar Extended-B
            ,
            BlockSpec.create("Myanmar Extended-B", 0xA9E0, 0xA9FF)
            // AA00..AA5F; Cham
            ,
            BlockSpec.create("Cham", 0xAA00, 0xAA5F)
            // AA60..AA7F; Myanmar Extended-A
            ,
            BlockSpec.create("Myanmar Extended-A", 0xAA60, 0xAA7F)
            // AA80..AADF; Tai Viet
            ,
            BlockSpec.create("Tai Viet", 0xAA80, 0xAADF)
            // AAE0..AAFF; Meetei Mayek Extensions
            ,
            BlockSpec.create("Meetei Mayek Extensions", 0xAAE0, 0xAAFF)
            // AB00..AB2F; Ethiopic Extended-A
            ,
            BlockSpec.create("Ethiopic Extended-A", 0xAB00, 0xAB2F)
            // AB30..AB6F; Latin Extended-E
            ,
            BlockSpec.create("Latin Extended-E", 0xAB30, 0xAB6F)
            // AB70..ABBF; Cherokee Supplement
            ,
            BlockSpec.create("Cherokee Supplement", 0xAB70, 0xABBF)
            // ABC0..ABFF; Meetei Mayek
            ,
            BlockSpec.create("Meetei Mayek", 0xABC0, 0xABFF)
            // AC00..D7AF; Hangul Syllables
            ,
            BlockSpec.create("Hangul Syllables", 0xAC00, 0xD7AF)
            // D7B0..D7FF; Hangul Jamo Extended-B
            ,
            BlockSpec.create("Hangul Jamo Extended-B", 0xD7B0, 0xD7FF)
            // E000..F8FF; Private Use Area
            ,
            BlockSpec.create("Private Use Area", 0xE000, 0xF8FF)
            // F900..FAFF; CJK Compatibility Ideographs
            ,
            BlockSpec.create("CJK Compatibility Ideographs", 0xF900, 0xFAFF)
            // FB00..FB4F; Alphabetic Presentation Forms
            ,
            BlockSpec.create("Alphabetic Presentation Forms", 0xFB00, 0xFB4F)
            // FB50..FDFF; Arabic Presentation Forms-A
            ,
            BlockSpec.create("Arabic Presentation Forms-A", 0xFB50, 0xFDFF)
            // FE00..FE0F; Variation Selectors
            ,
            BlockSpec.create("Variation Selectors", 0xFE00, 0xFE0F)
            // FE10..FE1F; Vertical Forms
            ,
            BlockSpec.create("Vertical Forms", 0xFE10, 0xFE1F)
            // FE20..FE2F; Combining Half Marks
            ,
            BlockSpec.create("Combining Half Marks", 0xFE20, 0xFE2F)
            // FE30..FE4F; CJK Compatibility Forms
            ,
            BlockSpec.create("CJK Compatibility Forms", 0xFE30, 0xFE4F)
            // FE50..FE6F; Small Form Variants
            ,
            BlockSpec.create("Small Form Variants", 0xFE50, 0xFE6F)
            // FE70..FEFF; Arabic Presentation Forms-B
            ,
            BlockSpec.create("Arabic Presentation Forms-B", 0xFE70, 0xFEFF)
            // FF00..FFEF; Halfwidth and Fullwidth Forms
            ,
            BlockSpec.create("Halfwidth and Fullwidth Forms", 0xFF00, 0xFFEF)
            // FFF0..FFFF; Specials
            ,
            BlockSpec.create("Specials", 0xFFF0, 0xFFFF)
            // 10000..1007F; Linear B Syllabary
            ,
            BlockSpec.create("Linear B Syllabary", 0x10000, 0x1007F)
            // 10080..100FF; Linear B Ideograms
            ,
            BlockSpec.create("Linear B Ideograms", 0x10080, 0x100FF)
            // 10100..1013F; Aegean Numbers
            ,
            BlockSpec.create("Aegean Numbers", 0x10100, 0x1013F)
            // 10140..1018F; Ancient Greek Numbers
            ,
            BlockSpec.create("Ancient Greek Numbers", 0x10140, 0x1018F)
            // 10190..101CF; Ancient Symbols
            ,
            BlockSpec.create("Ancient Symbols", 0x10190, 0x101CF)
            // 101D0..101FF; Phaistos Disc
            ,
            BlockSpec.create("Phaistos Disc", 0x101D0, 0x101FF)
            // 10280..1029F; Lycian
            ,
            BlockSpec.create("Lycian", 0x10280, 0x1029F)
            // 102A0..102DF; Carian
            ,
            BlockSpec.create("Carian", 0x102A0, 0x102DF)
            // 102E0..102FF; Coptic Epact Numbers
            ,
            BlockSpec.create("Coptic Epact Numbers", 0x102E0, 0x102FF)
            // 10300..1032F; Old Italic
            ,
            BlockSpec.create("Old Italic", 0x10300, 0x1032F)
            // 10330..1034F; Gothic
            ,
            BlockSpec.create("Gothic", 0x10330, 0x1034F)
            // 10350..1037F; Old Permic
            ,
            BlockSpec.create("Old Permic", 0x10350, 0x1037F)
            // 10380..1039F; Ugaritic
            ,
            BlockSpec.create("Ugaritic", 0x10380, 0x1039F)
            // 103A0..103DF; Old Persian
            ,
            BlockSpec.create("Old Persian", 0x103A0, 0x103DF)
            // 10400..1044F; Deseret
            ,
            BlockSpec.create("Deseret", 0x10400, 0x1044F)
            // 10450..1047F; Shavian
            ,
            BlockSpec.create("Shavian", 0x10450, 0x1047F)
            // 10480..104AF; Osmanya
            ,
            BlockSpec.create("Osmanya", 0x10480, 0x104AF)
            // 104B0..104FF; Osage
            ,
            BlockSpec.create("Osage", 0x104B0, 0x104FF)
            // 10500..1052F; Elbasan
            ,
            BlockSpec.create("Elbasan", 0x10500, 0x1052F)
            // 10530..1056F; Caucasian Albanian
            ,
            BlockSpec.create("Caucasian Albanian", 0x10530, 0x1056F)
            // 10600..1077F; Linear A
            ,
            BlockSpec.create("Linear A", 0x10600, 0x1077F)
            // 10800..1083F; Cypriot Syllabary
            ,
            BlockSpec.create("Cypriot Syllabary", 0x10800, 0x1083F)
            // 10840..1085F; Imperial Aramaic
            ,
            BlockSpec.create("Imperial Aramaic", 0x10840, 0x1085F)
            // 10860..1087F; Palmyrene
            ,
            BlockSpec.create("Palmyrene", 0x10860, 0x1087F)
            // 10880..108AF; Nabataean
            ,
            BlockSpec.create("Nabataean", 0x10880, 0x108AF)
            // 108E0..108FF; Hatran
            ,
            BlockSpec.create("Hatran", 0x108E0, 0x108FF)
            // 10900..1091F; Phoenician
            ,
            BlockSpec.create("Phoenician", 0x10900, 0x1091F)
            // 10920..1093F; Lydian
            ,
            BlockSpec.create("Lydian", 0x10920, 0x1093F)
            // 10980..1099F; Meroitic Hieroglyphs
            ,
            BlockSpec.create("Meroitic Hieroglyphs", 0x10980, 0x1099F)
            // 109A0..109FF; Meroitic Cursive
            ,
            BlockSpec.create("Meroitic Cursive", 0x109A0, 0x109FF)
            // 10A00..10A5F; Kharoshthi
            ,
            BlockSpec.create("Kharoshthi", 0x10A00, 0x10A5F)
            // 10A60..10A7F; Old South Arabian
            ,
            BlockSpec.create("Old South Arabian", 0x10A60, 0x10A7F)
            // 10A80..10A9F; Old North Arabian
            ,
            BlockSpec.create("Old North Arabian", 0x10A80, 0x10A9F)
            // 10AC0..10AFF; Manichaean
            ,
            BlockSpec.create("Manichaean", 0x10AC0, 0x10AFF)
            // 10B00..10B3F; Avestan
            ,
            BlockSpec.create("Avestan", 0x10B00, 0x10B3F)
            // 10B40..10B5F; Inscriptional Parthian
            ,
            BlockSpec.create("Inscriptional Parthian", 0x10B40, 0x10B5F)
            // 10B60..10B7F; Inscriptional Pahlavi
            ,
            BlockSpec.create("Inscriptional Pahlavi", 0x10B60, 0x10B7F)
            // 10B80..10BAF; Psalter Pahlavi
            ,
            BlockSpec.create("Psalter Pahlavi", 0x10B80, 0x10BAF)
            // 10C00..10C4F; Old Turkic
            ,
            BlockSpec.create("Old Turkic", 0x10C00, 0x10C4F)
            // 10C80..10CFF; Old Hungarian
            ,
            BlockSpec.create("Old Hungarian", 0x10C80, 0x10CFF)
            // 10E60..10E7F; Rumi Numeral Symbols
            ,
            BlockSpec.create("Rumi Numeral Symbols", 0x10E60, 0x10E7F)
            // 11000..1107F; Brahmi
            ,
            BlockSpec.create("Brahmi", 0x11000, 0x1107F)
            // 11080..110CF; Kaithi
            ,
            BlockSpec.create("Kaithi", 0x11080, 0x110CF)
            // 110D0..110FF; Sora Sompeng
            ,
            BlockSpec.create("Sora Sompeng", 0x110D0, 0x110FF)
            // 11100..1114F; Chakma
            ,
            BlockSpec.create("Chakma", 0x11100, 0x1114F)
            // 11150..1117F; Mahajani
            ,
            BlockSpec.create("Mahajani", 0x11150, 0x1117F)
            // 11180..111DF; Sharada
            ,
            BlockSpec.create("Sharada", 0x11180, 0x111DF)
            // 111E0..111FF; Sinhala Archaic Numbers
            ,
            BlockSpec.create("Sinhala Archaic Numbers", 0x111E0, 0x111FF)
            // 11200..1124F; Khojki
            ,
            BlockSpec.create("Khojki", 0x11200, 0x1124F)
            // 11280..112AF; Multani
            ,
            BlockSpec.create("Multani", 0x11280, 0x112AF)
            // 112B0..112FF; Khudawadi
            ,
            BlockSpec.create("Khudawadi", 0x112B0, 0x112FF)
            // 11300..1137F; Grantha
            ,
            BlockSpec.create("Grantha", 0x11300, 0x1137F)
            // 11400..1147F; Newa
            ,
            BlockSpec.create("Newa", 0x11400, 0x1147F)
            // 11480..114DF; Tirhuta
            ,
            BlockSpec.create("Tirhuta", 0x11480, 0x114DF)
            // 11580..115FF; Siddham
            ,
            BlockSpec.create("Siddham", 0x11580, 0x115FF)
            // 11600..1165F; Modi
            ,
            BlockSpec.create("Modi", 0x11600, 0x1165F)
            // 11660..1167F; Mongolian Supplement
            ,
            BlockSpec.create("Mongolian Supplement", 0x11660, 0x1167F)
            // 11680..116CF; Takri
            ,
            BlockSpec.create("Takri", 0x11680, 0x116CF)
            // 11700..1173F; Ahom
            ,
            BlockSpec.create("Ahom", 0x11700, 0x1173F)
            // 118A0..118FF; Warang Citi
            ,
            BlockSpec.create("Warang Citi", 0x118A0, 0x118FF)
            // 11AC0..11AFF; Pau Cin Hau
            ,
            BlockSpec.create("Pau Cin Hau", 0x11AC0, 0x11AFF)
            // 11C00..11C6F; Bhaiksuki
            ,
            BlockSpec.create("Bhaiksuki", 0x11C00, 0x11C6F)
            // 11C70..11CBF; Marchen
            ,
            BlockSpec.create("Marchen", 0x11C70, 0x11CBF)
            // 12000..123FF; Cuneiform
            ,
            BlockSpec.create("Cuneiform", 0x12000, 0x123FF)
            // 12400..1247F; Cuneiform Numbers and Punctuation
            ,
            BlockSpec.create("Cuneiform Numbers and Punctuation", 0x12400, 0x1247F)
            // 12480..1254F; Early Dynastic Cuneiform
            ,
            BlockSpec.create("Early Dynastic Cuneiform", 0x12480, 0x1254F)
            // 13000..1342F; Egyptian Hieroglyphs
            ,
            BlockSpec.create("Egyptian Hieroglyphs", 0x13000, 0x1342F)
            // 14400..1467F; Anatolian Hieroglyphs
            ,
            BlockSpec.create("Anatolian Hieroglyphs", 0x14400, 0x1467F)
            // 16800..16A3F; Bamum Supplement
            ,
            BlockSpec.create("Bamum Supplement", 0x16800, 0x16A3F)
            // 16A40..16A6F; Mro
            ,
            BlockSpec.create("Mro", 0x16A40, 0x16A6F)
            // 16AD0..16AFF; Bassa Vah
            ,
            BlockSpec.create("Bassa Vah", 0x16AD0, 0x16AFF)
            // 16B00..16B8F; Pahawh Hmong
            ,
            BlockSpec.create("Pahawh Hmong", 0x16B00, 0x16B8F)
            // 16F00..16F9F; Miao
            ,
            BlockSpec.create("Miao", 0x16F00, 0x16F9F)
            // 16FE0..16FFF; Ideographic Symbols and Punctuation
            ,
            BlockSpec.create("Ideographic Symbols and Punctuation", 0x16FE0, 0x16FFF)
            // 17000..187FF; Tangut
            ,
            BlockSpec.create("Tangut", 0x17000, 0x187FF)
            // 18800..18AFF; Tangut Components
            ,
            BlockSpec.create("Tangut Components", 0x18800, 0x18AFF)
            // 1B000..1B0FF; Kana Supplement
            ,
            BlockSpec.create("Kana Supplement", 0x1B000, 0x1B0FF)
            // 1BC00..1BC9F; Duployan
            ,
            BlockSpec.create("Duployan", 0x1BC00, 0x1BC9F)
            // 1BCA0..1BCAF; Shorthand Format Controls
            ,
            BlockSpec.create("Shorthand Format Controls", 0x1BCA0, 0x1BCAF)
            // 1D000..1D0FF; Byzantine Musical Symbols
            ,
            BlockSpec.create("Byzantine Musical Symbols", 0x1D000, 0x1D0FF)
            // 1D100..1D1FF; Musical Symbols
            ,
            BlockSpec.create("Musical Symbols", 0x1D100, 0x1D1FF)
            // 1D200..1D24F; Ancient Greek Musical Notation
            ,
            BlockSpec.create("Ancient Greek Musical Notation", 0x1D200, 0x1D24F)
            // 1D300..1D35F; Tai Xuan Jing Symbols
            ,
            BlockSpec.create("Tai Xuan Jing Symbols", 0x1D300, 0x1D35F)
            // 1D360..1D37F; Counting Rod Numerals
            ,
            BlockSpec.create("Counting Rod Numerals", 0x1D360, 0x1D37F)
            // 1D400..1D7FF; Mathematical Alphanumeric Symbols
            ,
            BlockSpec.create("Mathematical Alphanumeric Symbols", 0x1D400, 0x1D7FF)
            // 1D800..1DAAF; Sutton SignWriting
            ,
            BlockSpec.create("Sutton SignWriting", 0x1D800, 0x1DAAF)
            // 1E000..1E02F; Glagolitic Supplement
            ,
            BlockSpec.create("Glagolitic Supplement", 0x1E000, 0x1E02F)
            // 1E800..1E8DF; Mende Kikakui
            ,
            BlockSpec.create("Mende Kikakui", 0x1E800, 0x1E8DF)
            // 1E900..1E95F; Adlam
            ,
            BlockSpec.create("Adlam", 0x1E900, 0x1E95F)
            // 1EE00..1EEFF; Arabic Mathematical Alphabetic Symbols
            ,
            BlockSpec.create("Arabic Mathematical Alphabetic Symbols", 0x1EE00, 0x1EEFF)
            // 1F000..1F02F; Mahjong Tiles
            ,
            BlockSpec.create("Mahjong Tiles", 0x1F000, 0x1F02F)
            // 1F030..1F09F; Domino Tiles
            ,
            BlockSpec.create("Domino Tiles", 0x1F030, 0x1F09F)
            // 1F0A0..1F0FF; Playing Cards
            ,
            BlockSpec.create("Playing Cards", 0x1F0A0, 0x1F0FF)
            // 1F100..1F1FF; Enclosed Alphanumeric Supplement
            ,
            BlockSpec.create("Enclosed Alphanumeric Supplement", 0x1F100, 0x1F1FF)
            // 1F200..1F2FF; Enclosed Ideographic Supplement
            ,
            BlockSpec.create("Enclosed Ideographic Supplement", 0x1F200, 0x1F2FF)
            // 1F300..1F5FF; Miscellaneous Symbols and Pictographs
            ,
            BlockSpec.create("Miscellaneous Symbols and Pictographs", 0x1F300, 0x1F5FF)
            // 1F600..1F64F; Emoticons
            ,
            BlockSpec.create("Emoticons", 0x1F600, 0x1F64F)
            // 1F650..1F67F; Ornamental Dingbats
            ,
            BlockSpec.create("Ornamental Dingbats", 0x1F650, 0x1F67F)
            // 1F680..1F6FF; Transport and Map Symbols
            ,
            BlockSpec.create("Transport and Map Symbols", 0x1F680, 0x1F6FF)
            // 1F700..1F77F; Alchemical Symbols
            ,
            BlockSpec.create("Alchemical Symbols", 0x1F700, 0x1F77F)
            // 1F780..1F7FF; Geometric Shapes Extended
            ,
            BlockSpec.create("Geometric Shapes Extended", 0x1F780, 0x1F7FF)
            // 1F800..1F8FF; Supplemental Arrows-C
            ,
            BlockSpec.create("Supplemental Arrows-C", 0x1F800, 0x1F8FF)
            // 1F900..1F9FF; Supplemental Symbols and Pictographs
            ,
            BlockSpec.create("Supplemental Symbols and Pictographs", 0x1F900, 0x1F9FF)
            // 20000..2A6DF; CJK Unified Ideographs Extension B
            ,
            BlockSpec.create("CJK Unified Ideographs Extension B", 0x20000, 0x2A6DF)
            // 2A700..2B73F; CJK Unified Ideographs Extension C
            ,
            BlockSpec.create("CJK Unified Ideographs Extension C", 0x2A700, 0x2B73F)
            // 2B740..2B81F; CJK Unified Ideographs Extension D
            ,
            BlockSpec.create("CJK Unified Ideographs Extension D", 0x2B740, 0x2B81F)
            // 2B820..2CEAF; CJK Unified Ideographs Extension E
            ,
            BlockSpec.create("CJK Unified Ideographs Extension E", 0x2B820, 0x2CEAF)
            // 2F800..2FA1F; CJK Compatibility Ideographs Supplement
            ,
            BlockSpec.create("CJK Compatibility Ideographs Supplement", 0x2F800, 0x2FA1F)
            // E0000..E007F; Tags
            ,
            BlockSpec.create("Tags", 0xE0000, 0xE007F)
            // E0100..E01EF; Variation Selectors Supplement
            ,
            BlockSpec.create("Variation Selectors Supplement", 0xE0100, 0xE01EF)
            // F0000..FFFFF; Supplementary Private Use Area-A
            ,
            BlockSpec.create("Supplementary Private Use Area-A", 0xF0000, 0xFFFFF)
            // 100000..10FFFF; Supplementary Private Use Area-B
            ,
            BlockSpec.create("Supplementary Private Use Area-B", 0x100000, 0x10FFFF));
  }
}
