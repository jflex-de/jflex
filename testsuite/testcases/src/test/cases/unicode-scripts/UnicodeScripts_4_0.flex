%%

%unicode 4.0
%public
%class UnicodeScripts_4_0

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

\p{Arabic} { setCurCharPropertyValue("Arabic"); }
\p{Armenian} { setCurCharPropertyValue("Armenian"); }
\p{Bengali} { setCurCharPropertyValue("Bengali"); }
\p{Bopomofo} { setCurCharPropertyValue("Bopomofo"); }
\p{Braille} { setCurCharPropertyValue("Braille"); }
\p{Buhid} { setCurCharPropertyValue("Buhid"); }
\p{Canadian Aboriginal} { setCurCharPropertyValue("Canadian Aboriginal"); }
\p{Cherokee} { setCurCharPropertyValue("Cherokee"); }
\p{Common} { setCurCharPropertyValue("Common"); }
\p{Cypriot} { setCurCharPropertyValue("Cypriot"); }
\p{Cyrillic} { setCurCharPropertyValue("Cyrillic"); }
\p{Deseret} { setCurCharPropertyValue("Deseret"); }
\p{Devanagari} { setCurCharPropertyValue("Devanagari"); }
\p{Ethiopic} { setCurCharPropertyValue("Ethiopic"); }
\p{Georgian} { setCurCharPropertyValue("Georgian"); }
\p{Gothic} { setCurCharPropertyValue("Gothic"); }
\p{Greek} { setCurCharPropertyValue("Greek"); }
\p{Gujarati} { setCurCharPropertyValue("Gujarati"); }
\p{Gurmukhi} { setCurCharPropertyValue("Gurmukhi"); }
\p{Han} { setCurCharPropertyValue("Han"); }
\p{Hangul} { setCurCharPropertyValue("Hangul"); }
\p{Hanunoo} { setCurCharPropertyValue("Hanunoo"); }
\p{Hebrew} { setCurCharPropertyValue("Hebrew"); }
\p{Hiragana} { setCurCharPropertyValue("Hiragana"); }
\p{Inherited} { setCurCharPropertyValue("Inherited"); }
\p{Kannada} { setCurCharPropertyValue("Kannada"); }
\p{Katakana} { setCurCharPropertyValue("Katakana"); }
\p{Katakana Or Hiragana} { setCurCharPropertyValue("Katakana Or Hiragana"); }
\p{Khmer} { setCurCharPropertyValue("Khmer"); }
\p{Lao} { setCurCharPropertyValue("Lao"); }
\p{Latin} { setCurCharPropertyValue("Latin"); }
\p{Limbu} { setCurCharPropertyValue("Limbu"); }
\p{Linear B} { setCurCharPropertyValue("Linear B"); }
\p{Malayalam} { setCurCharPropertyValue("Malayalam"); }
\p{Mongolian} { setCurCharPropertyValue("Mongolian"); }
\p{Myanmar} { setCurCharPropertyValue("Myanmar"); }
\p{Ogham} { setCurCharPropertyValue("Ogham"); }
\p{Old Italic} { setCurCharPropertyValue("Old Italic"); }
\p{Oriya} { setCurCharPropertyValue("Oriya"); }
\p{Osmanya} { setCurCharPropertyValue("Osmanya"); }
\p{Runic} { setCurCharPropertyValue("Runic"); }
\p{Shavian} { setCurCharPropertyValue("Shavian"); }
\p{Sinhala} { setCurCharPropertyValue("Sinhala"); }
\p{Syriac} { setCurCharPropertyValue("Syriac"); }
\p{Tagalog} { setCurCharPropertyValue("Tagalog"); }
\p{Tagbanwa} { setCurCharPropertyValue("Tagbanwa"); }
\p{Tai Le} { setCurCharPropertyValue("Tai Le"); }
\p{Tamil} { setCurCharPropertyValue("Tamil"); }
\p{Telugu} { setCurCharPropertyValue("Telugu"); }
\p{Thaana} { setCurCharPropertyValue("Thaana"); }
\p{Thai} { setCurCharPropertyValue("Thai"); }
\p{Tibetan} { setCurCharPropertyValue("Tibetan"); }
\p{Ugaritic} { setCurCharPropertyValue("Ugaritic"); }
\p{Yi} { setCurCharPropertyValue("Yi"); }
<<EOF>> { printOutput(); return 1; }
