%%

%unicode 5.1
%public
%class UnicodeScriptAliases3_5_1

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java

%% 

\p{sc=Arabic} { setCurCharBlock("Arabic"); }
\p{sc=Armenian} { setCurCharBlock("Armenian"); }
\p{sc=Balinese} { setCurCharBlock("Balinese"); }
\p{sc=Bengali} { setCurCharBlock("Bengali"); }
\p{sc=Bopomofo} { setCurCharBlock("Bopomofo"); }
\p{sc=Braille} { setCurCharBlock("Braille"); }
\p{sc=Buginese} { setCurCharBlock("Buginese"); }
\p{sc=Buhid} { setCurCharBlock("Buhid"); }
\p{sc=Canadian Aboriginal} { setCurCharBlock("Canadian Aboriginal"); }
\p{sc=Carian} { setCurCharBlock("Carian"); }
\p{sc=Cham} { setCurCharBlock("Cham"); }
\p{sc=Cherokee} { setCurCharBlock("Cherokee"); }
\p{sc=Common} { setCurCharBlock("Common"); }
\p{sc=Coptic} { setCurCharBlock("Coptic"); }
\p{sc=Cuneiform} { setCurCharBlock("Cuneiform"); }
\p{sc=Cypriot} { setCurCharBlock("Cypriot"); }
\p{sc=Cyrillic} { setCurCharBlock("Cyrillic"); }
\p{sc=Deseret} { setCurCharBlock("Deseret"); }
\p{sc=Devanagari} { setCurCharBlock("Devanagari"); }
\p{sc=Ethiopic} { setCurCharBlock("Ethiopic"); }
\p{sc=Georgian} { setCurCharBlock("Georgian"); }
\p{sc=Glagolitic} { setCurCharBlock("Glagolitic"); }
\p{sc=Gothic} { setCurCharBlock("Gothic"); }
\p{sc=Greek} { setCurCharBlock("Greek"); }
\p{sc=Gujarati} { setCurCharBlock("Gujarati"); }
\p{sc=Gurmukhi} { setCurCharBlock("Gurmukhi"); }
\p{sc=Han} { setCurCharBlock("Han"); }
\p{sc=Hangul} { setCurCharBlock("Hangul"); }
\p{sc=Hanunoo} { setCurCharBlock("Hanunoo"); }
\p{sc=Hebrew} { setCurCharBlock("Hebrew"); }
\p{sc=Hiragana} { setCurCharBlock("Hiragana"); }
\p{sc=Inherited} { setCurCharBlock("Inherited"); }
\p{sc=Kannada} { setCurCharBlock("Kannada"); }
\p{sc=Katakana} { setCurCharBlock("Katakana"); }
\p{sc=Kayah Li} { setCurCharBlock("Kayah Li"); }
\p{sc=Kharoshthi} { setCurCharBlock("Kharoshthi"); }
\p{sc=Khmer} { setCurCharBlock("Khmer"); }
\p{sc=Lao} { setCurCharBlock("Lao"); }
\p{sc=Latin} { setCurCharBlock("Latin"); }
\p{sc=Lepcha} { setCurCharBlock("Lepcha"); }
\p{sc=Limbu} { setCurCharBlock("Limbu"); }
\p{sc=Linear B} { setCurCharBlock("Linear B"); }
\p{sc=Lycian} { setCurCharBlock("Lycian"); }
\p{sc=Lydian} { setCurCharBlock("Lydian"); }
\p{sc=Malayalam} { setCurCharBlock("Malayalam"); }
\p{sc=Mongolian} { setCurCharBlock("Mongolian"); }
\p{sc=Myanmar} { setCurCharBlock("Myanmar"); }
\p{sc=New Tai Lue} { setCurCharBlock("New Tai Lue"); }
\p{sc=Nko} { setCurCharBlock("Nko"); }
\p{sc=Ogham} { setCurCharBlock("Ogham"); }
\p{sc=Ol Chiki} { setCurCharBlock("Ol Chiki"); }
\p{sc=Old Italic} { setCurCharBlock("Old Italic"); }
\p{sc=Old Persian} { setCurCharBlock("Old Persian"); }
\p{sc=Oriya} { setCurCharBlock("Oriya"); }
\p{sc=Osmanya} { setCurCharBlock("Osmanya"); }
\p{sc=Phags Pa} { setCurCharBlock("Phags Pa"); }
\p{sc=Phoenician} { setCurCharBlock("Phoenician"); }
\p{sc=Rejang} { setCurCharBlock("Rejang"); }
\p{sc=Runic} { setCurCharBlock("Runic"); }
\p{sc=Saurashtra} { setCurCharBlock("Saurashtra"); }
\p{sc=Shavian} { setCurCharBlock("Shavian"); }
\p{sc=Sinhala} { setCurCharBlock("Sinhala"); }
\p{sc=Sundanese} { setCurCharBlock("Sundanese"); }
\p{sc=Syloti Nagri} { setCurCharBlock("Syloti Nagri"); }
\p{sc=Syriac} { setCurCharBlock("Syriac"); }
\p{sc=Tagalog} { setCurCharBlock("Tagalog"); }
\p{sc=Tagbanwa} { setCurCharBlock("Tagbanwa"); }
\p{sc=Tai Le} { setCurCharBlock("Tai Le"); }
\p{sc=Tamil} { setCurCharBlock("Tamil"); }
\p{sc=Telugu} { setCurCharBlock("Telugu"); }
\p{sc=Thaana} { setCurCharBlock("Thaana"); }
\p{sc=Thai} { setCurCharBlock("Thai"); }
\p{sc=Tibetan} { setCurCharBlock("Tibetan"); }
\p{sc=Tifinagh} { setCurCharBlock("Tifinagh"); }
\p{sc=Ugaritic} { setCurCharBlock("Ugaritic"); }
\p{sc=Unknown} { setCurCharBlock("Unknown"); }
\p{sc=Vai} { setCurCharBlock("Vai"); }
\p{sc=Yi} { setCurCharBlock("Yi"); }
<<EOF>> { printOutput(); return 1; }
