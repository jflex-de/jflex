%%

%unicode 3.1
%public
%class UnicodeScripts_3_1

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java  

%%

\p{Arabic} { setCurCharBlock("Arabic"); }
\p{Armenian} { setCurCharBlock("Armenian"); }
\p{Bengali} { setCurCharBlock("Bengali"); }
\p{Bopomofo} { setCurCharBlock("Bopomofo"); }
\p{Canadian Aboriginal} { setCurCharBlock("Canadian Aboriginal"); }
\p{Cherokee} { setCurCharBlock("Cherokee"); }
\p{Common} { setCurCharBlock("Common"); }
\p{Cyrillic} { setCurCharBlock("Cyrillic"); }
\p{Deseret} { setCurCharBlock("Deseret"); }
\p{Devanagari} { setCurCharBlock("Devanagari"); }
\p{Ethiopic} { setCurCharBlock("Ethiopic"); }
\p{Georgian} { setCurCharBlock("Georgian"); }
\p{Gothic} { setCurCharBlock("Gothic"); }
\p{Greek} { setCurCharBlock("Greek"); }
\p{Gujarati} { setCurCharBlock("Gujarati"); }
\p{Gurmukhi} { setCurCharBlock("Gurmukhi"); }
\p{Han} { setCurCharBlock("Han"); }
\p{Hangul} { setCurCharBlock("Hangul"); }
\p{Hebrew} { setCurCharBlock("Hebrew"); }
\p{Hiragana} { setCurCharBlock("Hiragana"); }
\p{Inherited} { setCurCharBlock("Inherited"); }
\p{Kannada} { setCurCharBlock("Kannada"); }
\p{Katakana} { setCurCharBlock("Katakana"); }
\p{Khmer} { setCurCharBlock("Khmer"); }
\p{Lao} { setCurCharBlock("Lao"); }
\p{Latin} { setCurCharBlock("Latin"); }
\p{Malayalam} { setCurCharBlock("Malayalam"); }
\p{Mongolian} { setCurCharBlock("Mongolian"); }
\p{Myanmar} { setCurCharBlock("Myanmar"); }
\p{Ogham} { setCurCharBlock("Ogham"); }
\p{Old Italic} { setCurCharBlock("Old Italic"); }
\p{Oriya} { setCurCharBlock("Oriya"); }
\p{Runic} { setCurCharBlock("Runic"); }
\p{Sinhala} { setCurCharBlock("Sinhala"); }
\p{Syriac} { setCurCharBlock("Syriac"); }
\p{Tamil} { setCurCharBlock("Tamil"); }
\p{Telugu} { setCurCharBlock("Telugu"); }
\p{Thaana} { setCurCharBlock("Thaana"); }
\p{Thai} { setCurCharBlock("Thai"); }
\p{Tibetan} { setCurCharBlock("Tibetan"); }
\p{Yi} { setCurCharBlock("Yi"); }
<<EOF>> { printOutput(); return 1; }
