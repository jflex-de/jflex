%%

%unicode 4.1
%public
%class UnicodeScripts_4_1

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java  

%%

\p{Arabic} { setCurCharBlock("Arabic"); }
\p{Armenian} { setCurCharBlock("Armenian"); }
\p{Bengali} { setCurCharBlock("Bengali"); }
\p{Bopomofo} { setCurCharBlock("Bopomofo"); }
\p{Braille} { setCurCharBlock("Braille"); }
\p{Buginese} { setCurCharBlock("Buginese"); }
\p{Buhid} { setCurCharBlock("Buhid"); }
\p{Canadian Aboriginal} { setCurCharBlock("Canadian Aboriginal"); }
\p{Cherokee} { setCurCharBlock("Cherokee"); }
\p{Common} { setCurCharBlock("Common"); }
\p{Coptic} { setCurCharBlock("Coptic"); }
\p{Cypriot} { setCurCharBlock("Cypriot"); }
\p{Cyrillic} { setCurCharBlock("Cyrillic"); }
\p{Deseret} { setCurCharBlock("Deseret"); }
\p{Devanagari} { setCurCharBlock("Devanagari"); }
\p{Ethiopic} { setCurCharBlock("Ethiopic"); }
\p{Georgian} { setCurCharBlock("Georgian"); }
\p{Glagolitic} { setCurCharBlock("Glagolitic"); }
\p{Gothic} { setCurCharBlock("Gothic"); }
\p{Greek} { setCurCharBlock("Greek"); }
\p{Gujarati} { setCurCharBlock("Gujarati"); }
\p{Gurmukhi} { setCurCharBlock("Gurmukhi"); }
\p{Han} { setCurCharBlock("Han"); }
\p{Hangul} { setCurCharBlock("Hangul"); }
\p{Hanunoo} { setCurCharBlock("Hanunoo"); }
\p{Hebrew} { setCurCharBlock("Hebrew"); }
\p{Hiragana} { setCurCharBlock("Hiragana"); }
\p{Inherited} { setCurCharBlock("Inherited"); }
\p{Kannada} { setCurCharBlock("Kannada"); }
\p{Katakana} { setCurCharBlock("Katakana"); }
\p{Kharoshthi} { setCurCharBlock("Kharoshthi"); }
\p{Khmer} { setCurCharBlock("Khmer"); }
\p{Lao} { setCurCharBlock("Lao"); }
\p{Latin} { setCurCharBlock("Latin"); }
\p{Limbu} { setCurCharBlock("Limbu"); }
\p{Linear B} { setCurCharBlock("Linear B"); }
\p{Malayalam} { setCurCharBlock("Malayalam"); }
\p{Mongolian} { setCurCharBlock("Mongolian"); }
\p{Myanmar} { setCurCharBlock("Myanmar"); }
\p{New Tai Lue} { setCurCharBlock("New Tai Lue"); }
\p{Ogham} { setCurCharBlock("Ogham"); }
\p{Old Italic} { setCurCharBlock("Old Italic"); }
\p{Old Persian} { setCurCharBlock("Old Persian"); }
\p{Oriya} { setCurCharBlock("Oriya"); }
\p{Osmanya} { setCurCharBlock("Osmanya"); }
\p{Runic} { setCurCharBlock("Runic"); }
\p{Shavian} { setCurCharBlock("Shavian"); }
\p{Sinhala} { setCurCharBlock("Sinhala"); }
\p{Syloti Nagri} { setCurCharBlock("Syloti Nagri"); }
\p{Syriac} { setCurCharBlock("Syriac"); }
\p{Tagalog} { setCurCharBlock("Tagalog"); }
\p{Tagbanwa} { setCurCharBlock("Tagbanwa"); }
\p{Tai Le} { setCurCharBlock("Tai Le"); }
\p{Tamil} { setCurCharBlock("Tamil"); }
\p{Telugu} { setCurCharBlock("Telugu"); }
\p{Thaana} { setCurCharBlock("Thaana"); }
\p{Thai} { setCurCharBlock("Thai"); }
\p{Tibetan} { setCurCharBlock("Tibetan"); }
\p{Tifinagh} { setCurCharBlock("Tifinagh"); }
\p{Ugaritic} { setCurCharBlock("Ugaritic"); }
\p{Yi} { setCurCharBlock("Yi"); }
<<EOF>> { printOutput(); return 1; }
