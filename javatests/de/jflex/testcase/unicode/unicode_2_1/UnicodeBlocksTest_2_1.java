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
package de.jflex.testcase.unicode.unicode_2_1;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test that parsing all Unicode codepoints detects the correct block ranges for Unicode 2.1. */
public class UnicodeBlocksTest_2_1 {
  @Test
  public void testBlocks() throws Exception {
    ImmutableList<BlockSpec> blocks =
        UnicodeDataScanners.getBlocks(
            ScannerFactory.of(UnicodeBlocks_2_1::new),
            UnicodeBlocks_2_1.YYEOF,
            UnicodeDataScanners.Dataset.BMP);
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
            // 0370..03FF; Greek
            ,
            BlockSpec.create("Greek", 0x0370, 0x03FF)
            // 0400..04FF; Cyrillic
            ,
            BlockSpec.create("Cyrillic", 0x0400, 0x04FF)
            // 0530..058F; Armenian
            ,
            BlockSpec.create("Armenian", 0x0530, 0x058F)
            // 0590..05FF; Hebrew
            ,
            BlockSpec.create("Hebrew", 0x0590, 0x05FF)
            // 0600..06FF; Arabic
            ,
            BlockSpec.create("Arabic", 0x0600, 0x06FF)
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
            // 0E00..0E7F; Thai
            ,
            BlockSpec.create("Thai", 0x0E00, 0x0E7F)
            // 0E80..0EFF; Lao
            ,
            BlockSpec.create("Lao", 0x0E80, 0x0EFF)
            // 0F00..0FBF; Tibetan
            ,
            BlockSpec.create("Tibetan", 0x0F00, 0x0FBF)
            // 10A0..10FF; Georgian
            ,
            BlockSpec.create("Georgian", 0x10A0, 0x10FF)
            // 1100..11FF; Hangul Jamo
            ,
            BlockSpec.create("Hangul Jamo", 0x1100, 0x11FF)
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
            // 20D0..20FF; Combining Marks for Symbols
            ,
            BlockSpec.create("Combining Marks for Symbols", 0x20D0, 0x20FF)
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
            // 3200..32FF; Enclosed CJK Letters and Months
            ,
            BlockSpec.create("Enclosed CJK Letters and Months", 0x3200, 0x32FF)
            // 3300..33FF; CJK Compatibility
            ,
            BlockSpec.create("CJK Compatibility", 0x3300, 0x33FF)
            // 4E00..9FFF; CJK Unified Ideographs
            ,
            BlockSpec.create("CJK Unified Ideographs", 0x4E00, 0x9FFF)
            // AC00..D7A3; Hangul Syllables
            ,
            BlockSpec.create("Hangul Syllables", 0xAC00, 0xD7A3)
            // E000..F8FF; Private Use
            ,
            BlockSpec.create("Private Use", 0xE000, 0xF8FF)
            // F900..FAFF; CJK Compatibility Ideographs
            ,
            BlockSpec.create("CJK Compatibility Ideographs", 0xF900, 0xFAFF)
            // FB00..FB4F; Alphabetic Presentation Forms
            ,
            BlockSpec.create("Alphabetic Presentation Forms", 0xFB00, 0xFB4F)
            // FB50..FDFF; Arabic Presentation Forms-A
            ,
            BlockSpec.create("Arabic Presentation Forms-A", 0xFB50, 0xFDFF)
            // FE20..FE2F; Combining Half Marks
            ,
            BlockSpec.create("Combining Half Marks", 0xFE20, 0xFE2F)
            // FE30..FE4F; CJK Compatibility Forms
            ,
            BlockSpec.create("CJK Compatibility Forms", 0xFE30, 0xFE4F)
            // FE50..FE6F; Small Form Variants
            ,
            BlockSpec.create("Small Form Variants", 0xFE50, 0xFE6F)
            // FE70..FEFE; Arabic Presentation Forms-B
            ,
            BlockSpec.create("Arabic Presentation Forms-B", 0xFE70, 0xFEFE)
            // FEFF..FEFF; Specials
            ,
            BlockSpec.create("Specials", 0xFEFF, 0xFEFF)
            // FF00..FFEF; Halfwidth and Fullwidth Forms
            ,
            BlockSpec.create("Halfwidth and Fullwidth Forms", 0xFF00, 0xFFEF)
            // FFF0..FFFD; Specials
            ,
            BlockSpec.create("Specials", 0xFFF0, 0xFFFD));
  }
}
