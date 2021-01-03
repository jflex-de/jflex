/*
 * Copyright (C) 2013 Steve Rowe <sarowe@gmail.com>
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
package de.jflex.testcase.unicode.unicode_11_0;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%
// Generated by UnicodeAgeGenerator from java/de/jflex/migration/unicodedatatest/testblock/UnicodeBlock.flex.vm

%unicode 11.0
%public
%class UnicodeBlocks_11_0
%extends AbstractEnumeratedPropertyDefinedScanner

%type int

%init{
  super(1114111);
%init}

%%

\p{Block:Adlam} { setCurCharPropertyValue(yytext(), "Adlam"); }
\p{Block:Aegean Numbers} { setCurCharPropertyValue(yytext(), "Aegean Numbers"); }
\p{Block:Ahom} { setCurCharPropertyValue(yytext(), "Ahom"); }
\p{Block:Alchemical Symbols} { setCurCharPropertyValue(yytext(), "Alchemical Symbols"); }
\p{Block:Alphabetic Presentation Forms} { setCurCharPropertyValue(yytext(), "Alphabetic Presentation Forms"); }
\p{Block:Anatolian Hieroglyphs} { setCurCharPropertyValue(yytext(), "Anatolian Hieroglyphs"); }
\p{Block:Ancient Greek Musical Notation} { setCurCharPropertyValue(yytext(), "Ancient Greek Musical Notation"); }
\p{Block:Ancient Greek Numbers} { setCurCharPropertyValue(yytext(), "Ancient Greek Numbers"); }
\p{Block:Ancient Symbols} { setCurCharPropertyValue(yytext(), "Ancient Symbols"); }
\p{Block:Arabic} { setCurCharPropertyValue(yytext(), "Arabic"); }
\p{Block:Arabic Extended-A} { setCurCharPropertyValue(yytext(), "Arabic Extended-A"); }
\p{Block:Arabic Mathematical Alphabetic Symbols} { setCurCharPropertyValue(yytext(), "Arabic Mathematical Alphabetic Symbols"); }
\p{Block:Arabic Presentation Forms-A} { setCurCharPropertyValue(yytext(), "Arabic Presentation Forms-A"); }
\p{Block:Arabic Presentation Forms-B} { setCurCharPropertyValue(yytext(), "Arabic Presentation Forms-B"); }
\p{Block:Arabic Supplement} { setCurCharPropertyValue(yytext(), "Arabic Supplement"); }
\p{Block:Armenian} { setCurCharPropertyValue(yytext(), "Armenian"); }
\p{Block:Arrows} { setCurCharPropertyValue(yytext(), "Arrows"); }
\p{Block:Avestan} { setCurCharPropertyValue(yytext(), "Avestan"); }
\p{Block:Balinese} { setCurCharPropertyValue(yytext(), "Balinese"); }
\p{Block:Bamum} { setCurCharPropertyValue(yytext(), "Bamum"); }
\p{Block:Bamum Supplement} { setCurCharPropertyValue(yytext(), "Bamum Supplement"); }
\p{Block:Basic Latin} { setCurCharPropertyValue(yytext(), "Basic Latin"); }
\p{Block:Bassa Vah} { setCurCharPropertyValue(yytext(), "Bassa Vah"); }
\p{Block:Batak} { setCurCharPropertyValue(yytext(), "Batak"); }
\p{Block:Bengali} { setCurCharPropertyValue(yytext(), "Bengali"); }
\p{Block:Bhaiksuki} { setCurCharPropertyValue(yytext(), "Bhaiksuki"); }
\p{Block:Block Elements} { setCurCharPropertyValue(yytext(), "Block Elements"); }
\p{Block:Bopomofo} { setCurCharPropertyValue(yytext(), "Bopomofo"); }
\p{Block:Bopomofo Extended} { setCurCharPropertyValue(yytext(), "Bopomofo Extended"); }
\p{Block:Box Drawing} { setCurCharPropertyValue(yytext(), "Box Drawing"); }
\p{Block:Brahmi} { setCurCharPropertyValue(yytext(), "Brahmi"); }
\p{Block:Braille Patterns} { setCurCharPropertyValue(yytext(), "Braille Patterns"); }
\p{Block:Buginese} { setCurCharPropertyValue(yytext(), "Buginese"); }
\p{Block:Buhid} { setCurCharPropertyValue(yytext(), "Buhid"); }
\p{Block:Byzantine Musical Symbols} { setCurCharPropertyValue(yytext(), "Byzantine Musical Symbols"); }
\p{Block:CJK Compatibility} { setCurCharPropertyValue(yytext(), "CJK Compatibility"); }
\p{Block:CJK Compatibility Forms} { setCurCharPropertyValue(yytext(), "CJK Compatibility Forms"); }
\p{Block:CJK Compatibility Ideographs} { setCurCharPropertyValue(yytext(), "CJK Compatibility Ideographs"); }
\p{Block:CJK Compatibility Ideographs Supplement} { setCurCharPropertyValue(yytext(), "CJK Compatibility Ideographs Supplement"); }
\p{Block:CJK Radicals Supplement} { setCurCharPropertyValue(yytext(), "CJK Radicals Supplement"); }
\p{Block:CJK Strokes} { setCurCharPropertyValue(yytext(), "CJK Strokes"); }
\p{Block:CJK Symbols and Punctuation} { setCurCharPropertyValue(yytext(), "CJK Symbols and Punctuation"); }
\p{Block:CJK Unified Ideographs} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs"); }
\p{Block:CJK Unified Ideographs Extension A} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs Extension A"); }
\p{Block:CJK Unified Ideographs Extension B} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs Extension B"); }
\p{Block:CJK Unified Ideographs Extension C} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs Extension C"); }
\p{Block:CJK Unified Ideographs Extension D} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs Extension D"); }
\p{Block:CJK Unified Ideographs Extension E} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs Extension E"); }
\p{Block:CJK Unified Ideographs Extension F} { setCurCharPropertyValue(yytext(), "CJK Unified Ideographs Extension F"); }
\p{Block:Carian} { setCurCharPropertyValue(yytext(), "Carian"); }
\p{Block:Caucasian Albanian} { setCurCharPropertyValue(yytext(), "Caucasian Albanian"); }
\p{Block:Chakma} { setCurCharPropertyValue(yytext(), "Chakma"); }
\p{Block:Cham} { setCurCharPropertyValue(yytext(), "Cham"); }
\p{Block:Cherokee} { setCurCharPropertyValue(yytext(), "Cherokee"); }
\p{Block:Cherokee Supplement} { setCurCharPropertyValue(yytext(), "Cherokee Supplement"); }
\p{Block:Chess Symbols} { setCurCharPropertyValue(yytext(), "Chess Symbols"); }
\p{Block:Combining Diacritical Marks} { setCurCharPropertyValue(yytext(), "Combining Diacritical Marks"); }
\p{Block:Combining Diacritical Marks Extended} { setCurCharPropertyValue(yytext(), "Combining Diacritical Marks Extended"); }
\p{Block:Combining Diacritical Marks Supplement} { setCurCharPropertyValue(yytext(), "Combining Diacritical Marks Supplement"); }
\p{Block:Combining Diacritical Marks for Symbols} { setCurCharPropertyValue(yytext(), "Combining Diacritical Marks for Symbols"); }
\p{Block:Combining Half Marks} { setCurCharPropertyValue(yytext(), "Combining Half Marks"); }
\p{Block:Common Indic Number Forms} { setCurCharPropertyValue(yytext(), "Common Indic Number Forms"); }
\p{Block:Control Pictures} { setCurCharPropertyValue(yytext(), "Control Pictures"); }
\p{Block:Coptic} { setCurCharPropertyValue(yytext(), "Coptic"); }
\p{Block:Coptic Epact Numbers} { setCurCharPropertyValue(yytext(), "Coptic Epact Numbers"); }
\p{Block:Counting Rod Numerals} { setCurCharPropertyValue(yytext(), "Counting Rod Numerals"); }
\p{Block:Cuneiform} { setCurCharPropertyValue(yytext(), "Cuneiform"); }
\p{Block:Cuneiform Numbers and Punctuation} { setCurCharPropertyValue(yytext(), "Cuneiform Numbers and Punctuation"); }
\p{Block:Currency Symbols} { setCurCharPropertyValue(yytext(), "Currency Symbols"); }
\p{Block:Cypriot Syllabary} { setCurCharPropertyValue(yytext(), "Cypriot Syllabary"); }
\p{Block:Cyrillic} { setCurCharPropertyValue(yytext(), "Cyrillic"); }
\p{Block:Cyrillic Extended-A} { setCurCharPropertyValue(yytext(), "Cyrillic Extended-A"); }
\p{Block:Cyrillic Extended-B} { setCurCharPropertyValue(yytext(), "Cyrillic Extended-B"); }
\p{Block:Cyrillic Extended-C} { setCurCharPropertyValue(yytext(), "Cyrillic Extended-C"); }
\p{Block:Cyrillic Supplement} { setCurCharPropertyValue(yytext(), "Cyrillic Supplement"); }
\p{Block:Deseret} { setCurCharPropertyValue(yytext(), "Deseret"); }
\p{Block:Devanagari} { setCurCharPropertyValue(yytext(), "Devanagari"); }
\p{Block:Devanagari Extended} { setCurCharPropertyValue(yytext(), "Devanagari Extended"); }
\p{Block:Dingbats} { setCurCharPropertyValue(yytext(), "Dingbats"); }
\p{Block:Dogra} { setCurCharPropertyValue(yytext(), "Dogra"); }
\p{Block:Domino Tiles} { setCurCharPropertyValue(yytext(), "Domino Tiles"); }
\p{Block:Duployan} { setCurCharPropertyValue(yytext(), "Duployan"); }
\p{Block:Early Dynastic Cuneiform} { setCurCharPropertyValue(yytext(), "Early Dynastic Cuneiform"); }
\p{Block:Egyptian Hieroglyphs} { setCurCharPropertyValue(yytext(), "Egyptian Hieroglyphs"); }
\p{Block:Elbasan} { setCurCharPropertyValue(yytext(), "Elbasan"); }
\p{Block:Emoticons} { setCurCharPropertyValue(yytext(), "Emoticons"); }
\p{Block:Enclosed Alphanumeric Supplement} { setCurCharPropertyValue(yytext(), "Enclosed Alphanumeric Supplement"); }
\p{Block:Enclosed Alphanumerics} { setCurCharPropertyValue(yytext(), "Enclosed Alphanumerics"); }
\p{Block:Enclosed CJK Letters and Months} { setCurCharPropertyValue(yytext(), "Enclosed CJK Letters and Months"); }
\p{Block:Enclosed Ideographic Supplement} { setCurCharPropertyValue(yytext(), "Enclosed Ideographic Supplement"); }
\p{Block:Ethiopic} { setCurCharPropertyValue(yytext(), "Ethiopic"); }
\p{Block:Ethiopic Extended} { setCurCharPropertyValue(yytext(), "Ethiopic Extended"); }
\p{Block:Ethiopic Extended-A} { setCurCharPropertyValue(yytext(), "Ethiopic Extended-A"); }
\p{Block:Ethiopic Supplement} { setCurCharPropertyValue(yytext(), "Ethiopic Supplement"); }
\p{Block:General Punctuation} { setCurCharPropertyValue(yytext(), "General Punctuation"); }
\p{Block:Geometric Shapes} { setCurCharPropertyValue(yytext(), "Geometric Shapes"); }
\p{Block:Geometric Shapes Extended} { setCurCharPropertyValue(yytext(), "Geometric Shapes Extended"); }
\p{Block:Georgian} { setCurCharPropertyValue(yytext(), "Georgian"); }
\p{Block:Georgian Extended} { setCurCharPropertyValue(yytext(), "Georgian Extended"); }
\p{Block:Georgian Supplement} { setCurCharPropertyValue(yytext(), "Georgian Supplement"); }
\p{Block:Glagolitic} { setCurCharPropertyValue(yytext(), "Glagolitic"); }
\p{Block:Glagolitic Supplement} { setCurCharPropertyValue(yytext(), "Glagolitic Supplement"); }
\p{Block:Gothic} { setCurCharPropertyValue(yytext(), "Gothic"); }
\p{Block:Grantha} { setCurCharPropertyValue(yytext(), "Grantha"); }
\p{Block:Greek Extended} { setCurCharPropertyValue(yytext(), "Greek Extended"); }
\p{Block:Greek and Coptic} { setCurCharPropertyValue(yytext(), "Greek and Coptic"); }
\p{Block:Gujarati} { setCurCharPropertyValue(yytext(), "Gujarati"); }
\p{Block:Gunjala Gondi} { setCurCharPropertyValue(yytext(), "Gunjala Gondi"); }
\p{Block:Gurmukhi} { setCurCharPropertyValue(yytext(), "Gurmukhi"); }
\p{Block:Halfwidth and Fullwidth Forms} { setCurCharPropertyValue(yytext(), "Halfwidth and Fullwidth Forms"); }
\p{Block:Hangul Compatibility Jamo} { setCurCharPropertyValue(yytext(), "Hangul Compatibility Jamo"); }
\p{Block:Hangul Jamo} { setCurCharPropertyValue(yytext(), "Hangul Jamo"); }
\p{Block:Hangul Jamo Extended-A} { setCurCharPropertyValue(yytext(), "Hangul Jamo Extended-A"); }
\p{Block:Hangul Jamo Extended-B} { setCurCharPropertyValue(yytext(), "Hangul Jamo Extended-B"); }
\p{Block:Hangul Syllables} { setCurCharPropertyValue(yytext(), "Hangul Syllables"); }
\p{Block:Hanifi Rohingya} { setCurCharPropertyValue(yytext(), "Hanifi Rohingya"); }
\p{Block:Hanunoo} { setCurCharPropertyValue(yytext(), "Hanunoo"); }
\p{Block:Hatran} { setCurCharPropertyValue(yytext(), "Hatran"); }
\p{Block:Hebrew} { setCurCharPropertyValue(yytext(), "Hebrew"); }
\p{Block:Hiragana} { setCurCharPropertyValue(yytext(), "Hiragana"); }
\p{Block:IPA Extensions} { setCurCharPropertyValue(yytext(), "IPA Extensions"); }
\p{Block:Ideographic Description Characters} { setCurCharPropertyValue(yytext(), "Ideographic Description Characters"); }
\p{Block:Ideographic Symbols and Punctuation} { setCurCharPropertyValue(yytext(), "Ideographic Symbols and Punctuation"); }
\p{Block:Imperial Aramaic} { setCurCharPropertyValue(yytext(), "Imperial Aramaic"); }
\p{Block:Indic Siyaq Numbers} { setCurCharPropertyValue(yytext(), "Indic Siyaq Numbers"); }
\p{Block:Inscriptional Pahlavi} { setCurCharPropertyValue(yytext(), "Inscriptional Pahlavi"); }
\p{Block:Inscriptional Parthian} { setCurCharPropertyValue(yytext(), "Inscriptional Parthian"); }
\p{Block:Javanese} { setCurCharPropertyValue(yytext(), "Javanese"); }
\p{Block:Kaithi} { setCurCharPropertyValue(yytext(), "Kaithi"); }
\p{Block:Kana Extended-A} { setCurCharPropertyValue(yytext(), "Kana Extended-A"); }
\p{Block:Kana Supplement} { setCurCharPropertyValue(yytext(), "Kana Supplement"); }
\p{Block:Kanbun} { setCurCharPropertyValue(yytext(), "Kanbun"); }
\p{Block:Kangxi Radicals} { setCurCharPropertyValue(yytext(), "Kangxi Radicals"); }
\p{Block:Kannada} { setCurCharPropertyValue(yytext(), "Kannada"); }
\p{Block:Katakana} { setCurCharPropertyValue(yytext(), "Katakana"); }
\p{Block:Katakana Phonetic Extensions} { setCurCharPropertyValue(yytext(), "Katakana Phonetic Extensions"); }
\p{Block:Kayah Li} { setCurCharPropertyValue(yytext(), "Kayah Li"); }
\p{Block:Kharoshthi} { setCurCharPropertyValue(yytext(), "Kharoshthi"); }
\p{Block:Khmer} { setCurCharPropertyValue(yytext(), "Khmer"); }
\p{Block:Khmer Symbols} { setCurCharPropertyValue(yytext(), "Khmer Symbols"); }
\p{Block:Khojki} { setCurCharPropertyValue(yytext(), "Khojki"); }
\p{Block:Khudawadi} { setCurCharPropertyValue(yytext(), "Khudawadi"); }
\p{Block:Lao} { setCurCharPropertyValue(yytext(), "Lao"); }
\p{Block:Latin Extended Additional} { setCurCharPropertyValue(yytext(), "Latin Extended Additional"); }
\p{Block:Latin Extended-A} { setCurCharPropertyValue(yytext(), "Latin Extended-A"); }
\p{Block:Latin Extended-B} { setCurCharPropertyValue(yytext(), "Latin Extended-B"); }
\p{Block:Latin Extended-C} { setCurCharPropertyValue(yytext(), "Latin Extended-C"); }
\p{Block:Latin Extended-D} { setCurCharPropertyValue(yytext(), "Latin Extended-D"); }
\p{Block:Latin Extended-E} { setCurCharPropertyValue(yytext(), "Latin Extended-E"); }
\p{Block:Latin-1 Supplement} { setCurCharPropertyValue(yytext(), "Latin-1 Supplement"); }
\p{Block:Lepcha} { setCurCharPropertyValue(yytext(), "Lepcha"); }
\p{Block:Letterlike Symbols} { setCurCharPropertyValue(yytext(), "Letterlike Symbols"); }
\p{Block:Limbu} { setCurCharPropertyValue(yytext(), "Limbu"); }
\p{Block:Linear A} { setCurCharPropertyValue(yytext(), "Linear A"); }
\p{Block:Linear B Ideograms} { setCurCharPropertyValue(yytext(), "Linear B Ideograms"); }
\p{Block:Linear B Syllabary} { setCurCharPropertyValue(yytext(), "Linear B Syllabary"); }
\p{Block:Lisu} { setCurCharPropertyValue(yytext(), "Lisu"); }
\p{Block:Lycian} { setCurCharPropertyValue(yytext(), "Lycian"); }
\p{Block:Lydian} { setCurCharPropertyValue(yytext(), "Lydian"); }
\p{Block:Mahajani} { setCurCharPropertyValue(yytext(), "Mahajani"); }
\p{Block:Mahjong Tiles} { setCurCharPropertyValue(yytext(), "Mahjong Tiles"); }
\p{Block:Makasar} { setCurCharPropertyValue(yytext(), "Makasar"); }
\p{Block:Malayalam} { setCurCharPropertyValue(yytext(), "Malayalam"); }
\p{Block:Mandaic} { setCurCharPropertyValue(yytext(), "Mandaic"); }
\p{Block:Manichaean} { setCurCharPropertyValue(yytext(), "Manichaean"); }
\p{Block:Marchen} { setCurCharPropertyValue(yytext(), "Marchen"); }
\p{Block:Masaram Gondi} { setCurCharPropertyValue(yytext(), "Masaram Gondi"); }
\p{Block:Mathematical Alphanumeric Symbols} { setCurCharPropertyValue(yytext(), "Mathematical Alphanumeric Symbols"); }
\p{Block:Mathematical Operators} { setCurCharPropertyValue(yytext(), "Mathematical Operators"); }
\p{Block:Mayan Numerals} { setCurCharPropertyValue(yytext(), "Mayan Numerals"); }
\p{Block:Medefaidrin} { setCurCharPropertyValue(yytext(), "Medefaidrin"); }
\p{Block:Meetei Mayek} { setCurCharPropertyValue(yytext(), "Meetei Mayek"); }
\p{Block:Meetei Mayek Extensions} { setCurCharPropertyValue(yytext(), "Meetei Mayek Extensions"); }
\p{Block:Mende Kikakui} { setCurCharPropertyValue(yytext(), "Mende Kikakui"); }
\p{Block:Meroitic Cursive} { setCurCharPropertyValue(yytext(), "Meroitic Cursive"); }
\p{Block:Meroitic Hieroglyphs} { setCurCharPropertyValue(yytext(), "Meroitic Hieroglyphs"); }
\p{Block:Miao} { setCurCharPropertyValue(yytext(), "Miao"); }
\p{Block:Miscellaneous Mathematical Symbols-A} { setCurCharPropertyValue(yytext(), "Miscellaneous Mathematical Symbols-A"); }
\p{Block:Miscellaneous Mathematical Symbols-B} { setCurCharPropertyValue(yytext(), "Miscellaneous Mathematical Symbols-B"); }
\p{Block:Miscellaneous Symbols} { setCurCharPropertyValue(yytext(), "Miscellaneous Symbols"); }
\p{Block:Miscellaneous Symbols and Arrows} { setCurCharPropertyValue(yytext(), "Miscellaneous Symbols and Arrows"); }
\p{Block:Miscellaneous Symbols and Pictographs} { setCurCharPropertyValue(yytext(), "Miscellaneous Symbols and Pictographs"); }
\p{Block:Miscellaneous Technical} { setCurCharPropertyValue(yytext(), "Miscellaneous Technical"); }
\p{Block:Modi} { setCurCharPropertyValue(yytext(), "Modi"); }
\p{Block:Modifier Tone Letters} { setCurCharPropertyValue(yytext(), "Modifier Tone Letters"); }
\p{Block:Mongolian} { setCurCharPropertyValue(yytext(), "Mongolian"); }
\p{Block:Mongolian Supplement} { setCurCharPropertyValue(yytext(), "Mongolian Supplement"); }
\p{Block:Mro} { setCurCharPropertyValue(yytext(), "Mro"); }
\p{Block:Multani} { setCurCharPropertyValue(yytext(), "Multani"); }
\p{Block:Musical Symbols} { setCurCharPropertyValue(yytext(), "Musical Symbols"); }
\p{Block:Myanmar} { setCurCharPropertyValue(yytext(), "Myanmar"); }
\p{Block:Myanmar Extended-A} { setCurCharPropertyValue(yytext(), "Myanmar Extended-A"); }
\p{Block:Myanmar Extended-B} { setCurCharPropertyValue(yytext(), "Myanmar Extended-B"); }
\p{Block:NKo} { setCurCharPropertyValue(yytext(), "NKo"); }
\p{Block:Nabataean} { setCurCharPropertyValue(yytext(), "Nabataean"); }
\p{Block:New Tai Lue} { setCurCharPropertyValue(yytext(), "New Tai Lue"); }
\p{Block:Newa} { setCurCharPropertyValue(yytext(), "Newa"); }
\p{Block:No Block} { setCurCharPropertyValue(yytext(), "No Block"); }
\p{Block:Number Forms} { setCurCharPropertyValue(yytext(), "Number Forms"); }
\p{Block:Nushu} { setCurCharPropertyValue(yytext(), "Nushu"); }
\p{Block:Ogham} { setCurCharPropertyValue(yytext(), "Ogham"); }
\p{Block:Ol Chiki} { setCurCharPropertyValue(yytext(), "Ol Chiki"); }
\p{Block:Old Hungarian} { setCurCharPropertyValue(yytext(), "Old Hungarian"); }
\p{Block:Old Italic} { setCurCharPropertyValue(yytext(), "Old Italic"); }
\p{Block:Old North Arabian} { setCurCharPropertyValue(yytext(), "Old North Arabian"); }
\p{Block:Old Permic} { setCurCharPropertyValue(yytext(), "Old Permic"); }
\p{Block:Old Persian} { setCurCharPropertyValue(yytext(), "Old Persian"); }
\p{Block:Old Sogdian} { setCurCharPropertyValue(yytext(), "Old Sogdian"); }
\p{Block:Old South Arabian} { setCurCharPropertyValue(yytext(), "Old South Arabian"); }
\p{Block:Old Turkic} { setCurCharPropertyValue(yytext(), "Old Turkic"); }
\p{Block:Optical Character Recognition} { setCurCharPropertyValue(yytext(), "Optical Character Recognition"); }
\p{Block:Oriya} { setCurCharPropertyValue(yytext(), "Oriya"); }
\p{Block:Ornamental Dingbats} { setCurCharPropertyValue(yytext(), "Ornamental Dingbats"); }
\p{Block:Osage} { setCurCharPropertyValue(yytext(), "Osage"); }
\p{Block:Osmanya} { setCurCharPropertyValue(yytext(), "Osmanya"); }
\p{Block:Pahawh Hmong} { setCurCharPropertyValue(yytext(), "Pahawh Hmong"); }
\p{Block:Palmyrene} { setCurCharPropertyValue(yytext(), "Palmyrene"); }
\p{Block:Pau Cin Hau} { setCurCharPropertyValue(yytext(), "Pau Cin Hau"); }
\p{Block:Phags-pa} { setCurCharPropertyValue(yytext(), "Phags-pa"); }
\p{Block:Phaistos Disc} { setCurCharPropertyValue(yytext(), "Phaistos Disc"); }
\p{Block:Phoenician} { setCurCharPropertyValue(yytext(), "Phoenician"); }
\p{Block:Phonetic Extensions} { setCurCharPropertyValue(yytext(), "Phonetic Extensions"); }
\p{Block:Phonetic Extensions Supplement} { setCurCharPropertyValue(yytext(), "Phonetic Extensions Supplement"); }
\p{Block:Playing Cards} { setCurCharPropertyValue(yytext(), "Playing Cards"); }
\p{Block:Private Use Area} { setCurCharPropertyValue(yytext(), "Private Use Area"); }
\p{Block:Psalter Pahlavi} { setCurCharPropertyValue(yytext(), "Psalter Pahlavi"); }
\p{Block:Rejang} { setCurCharPropertyValue(yytext(), "Rejang"); }
\p{Block:Rumi Numeral Symbols} { setCurCharPropertyValue(yytext(), "Rumi Numeral Symbols"); }
\p{Block:Runic} { setCurCharPropertyValue(yytext(), "Runic"); }
\p{Block:Samaritan} { setCurCharPropertyValue(yytext(), "Samaritan"); }
\p{Block:Saurashtra} { setCurCharPropertyValue(yytext(), "Saurashtra"); }
\p{Block:Sharada} { setCurCharPropertyValue(yytext(), "Sharada"); }
\p{Block:Shavian} { setCurCharPropertyValue(yytext(), "Shavian"); }
\p{Block:Shorthand Format Controls} { setCurCharPropertyValue(yytext(), "Shorthand Format Controls"); }
\p{Block:Siddham} { setCurCharPropertyValue(yytext(), "Siddham"); }
\p{Block:Sinhala} { setCurCharPropertyValue(yytext(), "Sinhala"); }
\p{Block:Sinhala Archaic Numbers} { setCurCharPropertyValue(yytext(), "Sinhala Archaic Numbers"); }
\p{Block:Small Form Variants} { setCurCharPropertyValue(yytext(), "Small Form Variants"); }
\p{Block:Sogdian} { setCurCharPropertyValue(yytext(), "Sogdian"); }
\p{Block:Sora Sompeng} { setCurCharPropertyValue(yytext(), "Sora Sompeng"); }
\p{Block:Soyombo} { setCurCharPropertyValue(yytext(), "Soyombo"); }
\p{Block:Spacing Modifier Letters} { setCurCharPropertyValue(yytext(), "Spacing Modifier Letters"); }
\p{Block:Specials} { setCurCharPropertyValue(yytext(), "Specials"); }
\p{Block:Sundanese} { setCurCharPropertyValue(yytext(), "Sundanese"); }
\p{Block:Sundanese Supplement} { setCurCharPropertyValue(yytext(), "Sundanese Supplement"); }
\p{Block:Superscripts and Subscripts} { setCurCharPropertyValue(yytext(), "Superscripts and Subscripts"); }
\p{Block:Supplemental Arrows-A} { setCurCharPropertyValue(yytext(), "Supplemental Arrows-A"); }
\p{Block:Supplemental Arrows-B} { setCurCharPropertyValue(yytext(), "Supplemental Arrows-B"); }
\p{Block:Supplemental Arrows-C} { setCurCharPropertyValue(yytext(), "Supplemental Arrows-C"); }
\p{Block:Supplemental Mathematical Operators} { setCurCharPropertyValue(yytext(), "Supplemental Mathematical Operators"); }
\p{Block:Supplemental Punctuation} { setCurCharPropertyValue(yytext(), "Supplemental Punctuation"); }
\p{Block:Supplemental Symbols and Pictographs} { setCurCharPropertyValue(yytext(), "Supplemental Symbols and Pictographs"); }
\p{Block:Supplementary Private Use Area-A} { setCurCharPropertyValue(yytext(), "Supplementary Private Use Area-A"); }
\p{Block:Supplementary Private Use Area-B} { setCurCharPropertyValue(yytext(), "Supplementary Private Use Area-B"); }
\p{Block:Sutton SignWriting} { setCurCharPropertyValue(yytext(), "Sutton SignWriting"); }
\p{Block:Syloti Nagri} { setCurCharPropertyValue(yytext(), "Syloti Nagri"); }
\p{Block:Syriac} { setCurCharPropertyValue(yytext(), "Syriac"); }
\p{Block:Syriac Supplement} { setCurCharPropertyValue(yytext(), "Syriac Supplement"); }
\p{Block:Tagalog} { setCurCharPropertyValue(yytext(), "Tagalog"); }
\p{Block:Tagbanwa} { setCurCharPropertyValue(yytext(), "Tagbanwa"); }
\p{Block:Tags} { setCurCharPropertyValue(yytext(), "Tags"); }
\p{Block:Tai Le} { setCurCharPropertyValue(yytext(), "Tai Le"); }
\p{Block:Tai Tham} { setCurCharPropertyValue(yytext(), "Tai Tham"); }
\p{Block:Tai Viet} { setCurCharPropertyValue(yytext(), "Tai Viet"); }
\p{Block:Tai Xuan Jing Symbols} { setCurCharPropertyValue(yytext(), "Tai Xuan Jing Symbols"); }
\p{Block:Takri} { setCurCharPropertyValue(yytext(), "Takri"); }
\p{Block:Tamil} { setCurCharPropertyValue(yytext(), "Tamil"); }
\p{Block:Tangut} { setCurCharPropertyValue(yytext(), "Tangut"); }
\p{Block:Tangut Components} { setCurCharPropertyValue(yytext(), "Tangut Components"); }
\p{Block:Telugu} { setCurCharPropertyValue(yytext(), "Telugu"); }
\p{Block:Thaana} { setCurCharPropertyValue(yytext(), "Thaana"); }
\p{Block:Thai} { setCurCharPropertyValue(yytext(), "Thai"); }
\p{Block:Tibetan} { setCurCharPropertyValue(yytext(), "Tibetan"); }
\p{Block:Tifinagh} { setCurCharPropertyValue(yytext(), "Tifinagh"); }
\p{Block:Tirhuta} { setCurCharPropertyValue(yytext(), "Tirhuta"); }
\p{Block:Transport and Map Symbols} { setCurCharPropertyValue(yytext(), "Transport and Map Symbols"); }
\p{Block:Ugaritic} { setCurCharPropertyValue(yytext(), "Ugaritic"); }
\p{Block:Unified Canadian Aboriginal Syllabics} { setCurCharPropertyValue(yytext(), "Unified Canadian Aboriginal Syllabics"); }
\p{Block:Unified Canadian Aboriginal Syllabics Extended} { setCurCharPropertyValue(yytext(), "Unified Canadian Aboriginal Syllabics Extended"); }
\p{Block:Vai} { setCurCharPropertyValue(yytext(), "Vai"); }
\p{Block:Variation Selectors} { setCurCharPropertyValue(yytext(), "Variation Selectors"); }
\p{Block:Variation Selectors Supplement} { setCurCharPropertyValue(yytext(), "Variation Selectors Supplement"); }
\p{Block:Vedic Extensions} { setCurCharPropertyValue(yytext(), "Vedic Extensions"); }
\p{Block:Vertical Forms} { setCurCharPropertyValue(yytext(), "Vertical Forms"); }
\p{Block:Warang Citi} { setCurCharPropertyValue(yytext(), "Warang Citi"); }
\p{Block:Yi Radicals} { setCurCharPropertyValue(yytext(), "Yi Radicals"); }
\p{Block:Yi Syllables} { setCurCharPropertyValue(yytext(), "Yi Syllables"); }
\p{Block:Yijing Hexagram Symbols} { setCurCharPropertyValue(yytext(), "Yijing Hexagram Symbols"); }
\p{Block:Zanabazar Square} { setCurCharPropertyValue(yytext(), "Zanabazar Square"); }

<<EOF>>     { return YYEOF;}
[^]         { }