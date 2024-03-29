/*
 * Copyright (C) 2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%
// Initially Generated by UnicodeAgeGenerator
// from java/de/jflex/migration/unicodedatatest/testblock/UnicodeBlock.flex.vm
// Manually modified to use `blk` alias instead of `block` canonical name.

%unicode 5.1
%public
%class UnicodeBlocksAliasesScanner
%extends AbstractEnumeratedPropertyDefinedScanner<String>

%type int

%init{
  super(/*maxCodepoint=*/ 0x10ffff, String.class);
%init}

%%

\p{blk=Aegean Numbers} { setCurCharPropertyValue(yytext(), yylength(), "Aegean Numbers"); }
\p{blk=Alphabetic Presentation Forms} { setCurCharPropertyValue(yytext(), yylength(), "Alphabetic Presentation Forms"); }
\p{blk=Ancient Greek Musical Notation} { setCurCharPropertyValue(yytext(), yylength(), "Ancient Greek Musical Notation"); }
\p{blk=Ancient Greek Numbers} { setCurCharPropertyValue(yytext(), yylength(), "Ancient Greek Numbers"); }
\p{blk=Ancient Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Ancient Symbols"); }
\p{blk=Arabic} { setCurCharPropertyValue(yytext(), yylength(), "Arabic"); }
\p{blk=Arabic Presentation Forms-A} { setCurCharPropertyValue(yytext(), yylength(), "Arabic Presentation Forms-A"); }
\p{blk=Arabic Presentation Forms-B} { setCurCharPropertyValue(yytext(), yylength(), "Arabic Presentation Forms-B"); }
\p{blk=Arabic Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Arabic Supplement"); }
\p{blk=Armenian} { setCurCharPropertyValue(yytext(), yylength(), "Armenian"); }
\p{blk=Arrows} { setCurCharPropertyValue(yytext(), yylength(), "Arrows"); }
\p{blk=Balinese} { setCurCharPropertyValue(yytext(), yylength(), "Balinese"); }
\p{blk=Basic Latin} { setCurCharPropertyValue(yytext(), yylength(), "Basic Latin"); }
\p{blk=Bengali} { setCurCharPropertyValue(yytext(), yylength(), "Bengali"); }
\p{blk=Block Elements} { setCurCharPropertyValue(yytext(), yylength(), "Block Elements"); }
\p{blk=Bopomofo} { setCurCharPropertyValue(yytext(), yylength(), "Bopomofo"); }
\p{blk=Bopomofo Extended} { setCurCharPropertyValue(yytext(), yylength(), "Bopomofo Extended"); }
\p{blk=Box Drawing} { setCurCharPropertyValue(yytext(), yylength(), "Box Drawing"); }
\p{blk=Braille Patterns} { setCurCharPropertyValue(yytext(), yylength(), "Braille Patterns"); }
\p{blk=Buginese} { setCurCharPropertyValue(yytext(), yylength(), "Buginese"); }
\p{blk=Buhid} { setCurCharPropertyValue(yytext(), yylength(), "Buhid"); }
\p{blk=Byzantine Musical Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Byzantine Musical Symbols"); }
\p{blk=CJK Compatibility} { setCurCharPropertyValue(yytext(), yylength(), "CJK Compatibility"); }
\p{blk=CJK Compatibility Forms} { setCurCharPropertyValue(yytext(), yylength(), "CJK Compatibility Forms"); }
\p{blk=CJK Compatibility Ideographs} { setCurCharPropertyValue(yytext(), yylength(), "CJK Compatibility Ideographs"); }
\p{blk=CJK Compatibility Ideographs Supplement} { setCurCharPropertyValue(yytext(), yylength(), "CJK Compatibility Ideographs Supplement"); }
\p{blk=CJK Radicals Supplement} { setCurCharPropertyValue(yytext(), yylength(), "CJK Radicals Supplement"); }
\p{blk=CJK Strokes} { setCurCharPropertyValue(yytext(), yylength(), "CJK Strokes"); }
\p{blk=CJK Symbols and Punctuation} { setCurCharPropertyValue(yytext(), yylength(), "CJK Symbols and Punctuation"); }
\p{blk=CJK Unified Ideographs} { setCurCharPropertyValue(yytext(), yylength(), "CJK Unified Ideographs"); }
\p{blk=CJK Unified Ideographs Extension A} { setCurCharPropertyValue(yytext(), yylength(), "CJK Unified Ideographs Extension A"); }
\p{blk=CJK Unified Ideographs Extension B} { setCurCharPropertyValue(yytext(), yylength(), "CJK Unified Ideographs Extension B"); }
\p{blk=Carian} { setCurCharPropertyValue(yytext(), yylength(), "Carian"); }
\p{blk=Cham} { setCurCharPropertyValue(yytext(), yylength(), "Cham"); }
\p{blk=Cherokee} { setCurCharPropertyValue(yytext(), yylength(), "Cherokee"); }
\p{blk=Combining Diacritical Marks} { setCurCharPropertyValue(yytext(), yylength(), "Combining Diacritical Marks"); }
\p{blk=Combining Diacritical Marks Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Combining Diacritical Marks Supplement"); }
\p{blk=Combining Diacritical Marks for Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Combining Diacritical Marks for Symbols"); }
\p{blk=Combining Half Marks} { setCurCharPropertyValue(yytext(), yylength(), "Combining Half Marks"); }
\p{blk=Control Pictures} { setCurCharPropertyValue(yytext(), yylength(), "Control Pictures"); }
\p{blk=Coptic} { setCurCharPropertyValue(yytext(), yylength(), "Coptic"); }
\p{blk=Counting Rod Numerals} { setCurCharPropertyValue(yytext(), yylength(), "Counting Rod Numerals"); }
\p{blk=Cuneiform} { setCurCharPropertyValue(yytext(), yylength(), "Cuneiform"); }
\p{blk=Cuneiform Numbers and Punctuation} { setCurCharPropertyValue(yytext(), yylength(), "Cuneiform Numbers and Punctuation"); }
\p{blk=Currency Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Currency Symbols"); }
\p{blk=Cypriot Syllabary} { setCurCharPropertyValue(yytext(), yylength(), "Cypriot Syllabary"); }
\p{blk=Cyrillic} { setCurCharPropertyValue(yytext(), yylength(), "Cyrillic"); }
\p{blk=Cyrillic Extended-A} { setCurCharPropertyValue(yytext(), yylength(), "Cyrillic Extended-A"); }
\p{blk=Cyrillic Extended-B} { setCurCharPropertyValue(yytext(), yylength(), "Cyrillic Extended-B"); }
\p{blk=Cyrillic Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Cyrillic Supplement"); }
\p{blk=Deseret} { setCurCharPropertyValue(yytext(), yylength(), "Deseret"); }
\p{blk=Devanagari} { setCurCharPropertyValue(yytext(), yylength(), "Devanagari"); }
\p{blk=Dingbats} { setCurCharPropertyValue(yytext(), yylength(), "Dingbats"); }
\p{blk=Domino Tiles} { setCurCharPropertyValue(yytext(), yylength(), "Domino Tiles"); }
\p{blk=Enclosed Alphanumerics} { setCurCharPropertyValue(yytext(), yylength(), "Enclosed Alphanumerics"); }
\p{blk=Enclosed CJK Letters and Months} { setCurCharPropertyValue(yytext(), yylength(), "Enclosed CJK Letters and Months"); }
\p{blk=Ethiopic} { setCurCharPropertyValue(yytext(), yylength(), "Ethiopic"); }
\p{blk=Ethiopic Extended} { setCurCharPropertyValue(yytext(), yylength(), "Ethiopic Extended"); }
\p{blk=Ethiopic Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Ethiopic Supplement"); }
\p{blk=General Punctuation} { setCurCharPropertyValue(yytext(), yylength(), "General Punctuation"); }
\p{blk=Geometric Shapes} { setCurCharPropertyValue(yytext(), yylength(), "Geometric Shapes"); }
\p{blk=Georgian} { setCurCharPropertyValue(yytext(), yylength(), "Georgian"); }
\p{blk=Georgian Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Georgian Supplement"); }
\p{blk=Glagolitic} { setCurCharPropertyValue(yytext(), yylength(), "Glagolitic"); }
\p{blk=Gothic} { setCurCharPropertyValue(yytext(), yylength(), "Gothic"); }
\p{blk=Greek Extended} { setCurCharPropertyValue(yytext(), yylength(), "Greek Extended"); }
\p{blk=Greek and Coptic} { setCurCharPropertyValue(yytext(), yylength(), "Greek and Coptic"); }
\p{blk=Gujarati} { setCurCharPropertyValue(yytext(), yylength(), "Gujarati"); }
\p{blk=Gurmukhi} { setCurCharPropertyValue(yytext(), yylength(), "Gurmukhi"); }
\p{blk=Halfwidth and Fullwidth Forms} { setCurCharPropertyValue(yytext(), yylength(), "Halfwidth and Fullwidth Forms"); }
\p{blk=Hangul Compatibility Jamo} { setCurCharPropertyValue(yytext(), yylength(), "Hangul Compatibility Jamo"); }
\p{blk=Hangul Jamo} { setCurCharPropertyValue(yytext(), yylength(), "Hangul Jamo"); }
\p{blk=Hangul Syllables} { setCurCharPropertyValue(yytext(), yylength(), "Hangul Syllables"); }
\p{blk=Hanunoo} { setCurCharPropertyValue(yytext(), yylength(), "Hanunoo"); }
\p{blk=Hebrew} { setCurCharPropertyValue(yytext(), yylength(), "Hebrew"); }
\p{blk=Hiragana} { setCurCharPropertyValue(yytext(), yylength(), "Hiragana"); }
\p{blk=IPA Extensions} { setCurCharPropertyValue(yytext(), yylength(), "IPA Extensions"); }
\p{blk=Ideographic Description Characters} { setCurCharPropertyValue(yytext(), yylength(), "Ideographic Description Characters"); }
\p{blk=Kanbun} { setCurCharPropertyValue(yytext(), yylength(), "Kanbun"); }
\p{blk=Kangxi Radicals} { setCurCharPropertyValue(yytext(), yylength(), "Kangxi Radicals"); }
\p{blk=Kannada} { setCurCharPropertyValue(yytext(), yylength(), "Kannada"); }
\p{blk=Katakana} { setCurCharPropertyValue(yytext(), yylength(), "Katakana"); }
\p{blk=Katakana Phonetic Extensions} { setCurCharPropertyValue(yytext(), yylength(), "Katakana Phonetic Extensions"); }
\p{blk=Kayah Li} { setCurCharPropertyValue(yytext(), yylength(), "Kayah Li"); }
\p{blk=Kharoshthi} { setCurCharPropertyValue(yytext(), yylength(), "Kharoshthi"); }
\p{blk=Khmer} { setCurCharPropertyValue(yytext(), yylength(), "Khmer"); }
\p{blk=Khmer Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Khmer Symbols"); }
\p{blk=Lao} { setCurCharPropertyValue(yytext(), yylength(), "Lao"); }
\p{blk=Latin Extended Additional} { setCurCharPropertyValue(yytext(), yylength(), "Latin Extended Additional"); }
\p{blk=Latin Extended-A} { setCurCharPropertyValue(yytext(), yylength(), "Latin Extended-A"); }
\p{blk=Latin Extended-B} { setCurCharPropertyValue(yytext(), yylength(), "Latin Extended-B"); }
\p{blk=Latin Extended-C} { setCurCharPropertyValue(yytext(), yylength(), "Latin Extended-C"); }
\p{blk=Latin Extended-D} { setCurCharPropertyValue(yytext(), yylength(), "Latin Extended-D"); }
\p{blk=Latin-1 Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Latin-1 Supplement"); }
\p{blk=Lepcha} { setCurCharPropertyValue(yytext(), yylength(), "Lepcha"); }
\p{blk=Letterlike Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Letterlike Symbols"); }
\p{blk=Limbu} { setCurCharPropertyValue(yytext(), yylength(), "Limbu"); }
\p{blk=Linear B Ideograms} { setCurCharPropertyValue(yytext(), yylength(), "Linear B Ideograms"); }
\p{blk=Linear B Syllabary} { setCurCharPropertyValue(yytext(), yylength(), "Linear B Syllabary"); }
\p{blk=Lycian} { setCurCharPropertyValue(yytext(), yylength(), "Lycian"); }
\p{blk=Lydian} { setCurCharPropertyValue(yytext(), yylength(), "Lydian"); }
\p{blk=Mahjong Tiles} { setCurCharPropertyValue(yytext(), yylength(), "Mahjong Tiles"); }
\p{blk=Malayalam} { setCurCharPropertyValue(yytext(), yylength(), "Malayalam"); }
\p{blk=Mathematical Alphanumeric Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Mathematical Alphanumeric Symbols"); }
\p{blk=Mathematical Operators} { setCurCharPropertyValue(yytext(), yylength(), "Mathematical Operators"); }
\p{blk=Miscellaneous Mathematical Symbols-A} { setCurCharPropertyValue(yytext(), yylength(), "Miscellaneous Mathematical Symbols-A"); }
\p{blk=Miscellaneous Mathematical Symbols-B} { setCurCharPropertyValue(yytext(), yylength(), "Miscellaneous Mathematical Symbols-B"); }
\p{blk=Miscellaneous Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Miscellaneous Symbols"); }
\p{blk=Miscellaneous Symbols and Arrows} { setCurCharPropertyValue(yytext(), yylength(), "Miscellaneous Symbols and Arrows"); }
\p{blk=Miscellaneous Technical} { setCurCharPropertyValue(yytext(), yylength(), "Miscellaneous Technical"); }
\p{blk=Modifier Tone Letters} { setCurCharPropertyValue(yytext(), yylength(), "Modifier Tone Letters"); }
\p{blk=Mongolian} { setCurCharPropertyValue(yytext(), yylength(), "Mongolian"); }
\p{blk=Musical Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Musical Symbols"); }
\p{blk=Myanmar} { setCurCharPropertyValue(yytext(), yylength(), "Myanmar"); }
\p{blk=NKo} { setCurCharPropertyValue(yytext(), yylength(), "NKo"); }
\p{blk=New Tai Lue} { setCurCharPropertyValue(yytext(), yylength(), "New Tai Lue"); }
\p{blk=No Block} { setCurCharPropertyValue(yytext(), yylength(), "No Block"); }
\p{blk=Number Forms} { setCurCharPropertyValue(yytext(), yylength(), "Number Forms"); }
\p{blk=Ogham} { setCurCharPropertyValue(yytext(), yylength(), "Ogham"); }
\p{blk=Ol Chiki} { setCurCharPropertyValue(yytext(), yylength(), "Ol Chiki"); }
\p{blk=Old Italic} { setCurCharPropertyValue(yytext(), yylength(), "Old Italic"); }
\p{blk=Old Persian} { setCurCharPropertyValue(yytext(), yylength(), "Old Persian"); }
\p{blk=Optical Character Recognition} { setCurCharPropertyValue(yytext(), yylength(), "Optical Character Recognition"); }
\p{blk=Oriya} { setCurCharPropertyValue(yytext(), yylength(), "Oriya"); }
\p{blk=Osmanya} { setCurCharPropertyValue(yytext(), yylength(), "Osmanya"); }
\p{blk=Phags-pa} { setCurCharPropertyValue(yytext(), yylength(), "Phags-pa"); }
\p{blk=Phaistos Disc} { setCurCharPropertyValue(yytext(), yylength(), "Phaistos Disc"); }
\p{blk=Phoenician} { setCurCharPropertyValue(yytext(), yylength(), "Phoenician"); }
\p{blk=Phonetic Extensions} { setCurCharPropertyValue(yytext(), yylength(), "Phonetic Extensions"); }
\p{blk=Phonetic Extensions Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Phonetic Extensions Supplement"); }
\p{blk=Private Use Area} { setCurCharPropertyValue(yytext(), yylength(), "Private Use Area"); }
\p{blk=Rejang} { setCurCharPropertyValue(yytext(), yylength(), "Rejang"); }
\p{blk=Runic} { setCurCharPropertyValue(yytext(), yylength(), "Runic"); }
\p{blk=Saurashtra} { setCurCharPropertyValue(yytext(), yylength(), "Saurashtra"); }
\p{blk=Shavian} { setCurCharPropertyValue(yytext(), yylength(), "Shavian"); }
\p{blk=Sinhala} { setCurCharPropertyValue(yytext(), yylength(), "Sinhala"); }
\p{blk=Small Form Variants} { setCurCharPropertyValue(yytext(), yylength(), "Small Form Variants"); }
\p{blk=Spacing Modifier Letters} { setCurCharPropertyValue(yytext(), yylength(), "Spacing Modifier Letters"); }
\p{blk=Specials} { setCurCharPropertyValue(yytext(), yylength(), "Specials"); }
\p{blk=Sundanese} { setCurCharPropertyValue(yytext(), yylength(), "Sundanese"); }
\p{blk=Superscripts and Subscripts} { setCurCharPropertyValue(yytext(), yylength(), "Superscripts and Subscripts"); }
\p{blk=Supplemental Arrows-A} { setCurCharPropertyValue(yytext(), yylength(), "Supplemental Arrows-A"); }
\p{blk=Supplemental Arrows-B} { setCurCharPropertyValue(yytext(), yylength(), "Supplemental Arrows-B"); }
\p{blk=Supplemental Mathematical Operators} { setCurCharPropertyValue(yytext(), yylength(), "Supplemental Mathematical Operators"); }
\p{blk=Supplemental Punctuation} { setCurCharPropertyValue(yytext(), yylength(), "Supplemental Punctuation"); }
\p{blk=Supplementary Private Use Area-A} { setCurCharPropertyValue(yytext(), yylength(), "Supplementary Private Use Area-A"); }
\p{blk=Supplementary Private Use Area-B} { setCurCharPropertyValue(yytext(), yylength(), "Supplementary Private Use Area-B"); }
\p{blk=Syloti Nagri} { setCurCharPropertyValue(yytext(), yylength(), "Syloti Nagri"); }
\p{blk=Syriac} { setCurCharPropertyValue(yytext(), yylength(), "Syriac"); }
\p{blk=Tagalog} { setCurCharPropertyValue(yytext(), yylength(), "Tagalog"); }
\p{blk=Tagbanwa} { setCurCharPropertyValue(yytext(), yylength(), "Tagbanwa"); }
\p{blk=Tags} { setCurCharPropertyValue(yytext(), yylength(), "Tags"); }
\p{blk=Tai Le} { setCurCharPropertyValue(yytext(), yylength(), "Tai Le"); }
\p{blk=Tai Xuan Jing Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Tai Xuan Jing Symbols"); }
\p{blk=Tamil} { setCurCharPropertyValue(yytext(), yylength(), "Tamil"); }
\p{blk=Telugu} { setCurCharPropertyValue(yytext(), yylength(), "Telugu"); }
\p{blk=Thaana} { setCurCharPropertyValue(yytext(), yylength(), "Thaana"); }
\p{blk=Thai} { setCurCharPropertyValue(yytext(), yylength(), "Thai"); }
\p{blk=Tibetan} { setCurCharPropertyValue(yytext(), yylength(), "Tibetan"); }
\p{blk=Tifinagh} { setCurCharPropertyValue(yytext(), yylength(), "Tifinagh"); }
\p{blk=Ugaritic} { setCurCharPropertyValue(yytext(), yylength(), "Ugaritic"); }
\p{blk=Unified Canadian Aboriginal Syllabics} { setCurCharPropertyValue(yytext(), yylength(), "Unified Canadian Aboriginal Syllabics"); }
\p{blk=Vai} { setCurCharPropertyValue(yytext(), yylength(), "Vai"); }
\p{blk=Variation Selectors} { setCurCharPropertyValue(yytext(), yylength(), "Variation Selectors"); }
\p{blk=Variation Selectors Supplement} { setCurCharPropertyValue(yytext(), yylength(), "Variation Selectors Supplement"); }
\p{blk=Vertical Forms} { setCurCharPropertyValue(yytext(), yylength(), "Vertical Forms"); }
\p{blk=Yi Radicals} { setCurCharPropertyValue(yytext(), yylength(), "Yi Radicals"); }
\p{blk=Yi Syllables} { setCurCharPropertyValue(yytext(), yylength(), "Yi Syllables"); }
\p{blk=Yijing Hexagram Symbols} { setCurCharPropertyValue(yytext(), yylength(), "Yijing Hexagram Symbols"); }

<<EOF>>     { return YYEOF;}
[^]         { }
