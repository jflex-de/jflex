%%

%unicode 5.0
%public
%class UnicodeScripts_5_0

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

\p{Arabic} { setCurCharPropertyValue("Arabic"); }
\p{Armenian} { setCurCharPropertyValue("Armenian"); }
\p{Balinese} { setCurCharPropertyValue("Balinese"); }
\p{Bengali} { setCurCharPropertyValue("Bengali"); }
\p{Bopomofo} { setCurCharPropertyValue("Bopomofo"); }
\p{Braille} { setCurCharPropertyValue("Braille"); }
\p{Buginese} { setCurCharPropertyValue("Buginese"); }
\p{Buhid} { setCurCharPropertyValue("Buhid"); }
\p{Canadian Aboriginal} { setCurCharPropertyValue("Canadian Aboriginal"); }
\p{Cherokee} { setCurCharPropertyValue("Cherokee"); }
\p{Common} { setCurCharPropertyValue("Common"); }
\p{Coptic} { setCurCharPropertyValue("Coptic"); }
\p{Cuneiform} { setCurCharPropertyValue("Cuneiform"); }
\p{Cypriot} { setCurCharPropertyValue("Cypriot"); }
\p{Cyrillic} { setCurCharPropertyValue("Cyrillic"); }
\p{Deseret} { setCurCharPropertyValue("Deseret"); }
\p{Devanagari} { setCurCharPropertyValue("Devanagari"); }
\p{Ethiopic} { setCurCharPropertyValue("Ethiopic"); }
\p{Georgian} { setCurCharPropertyValue("Georgian"); }
\p{Glagolitic} { setCurCharPropertyValue("Glagolitic"); }
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
\p{Kharoshthi} { setCurCharPropertyValue("Kharoshthi"); }
\p{Khmer} { setCurCharPropertyValue("Khmer"); }
\p{Lao} { setCurCharPropertyValue("Lao"); }
\p{Latin} { setCurCharPropertyValue("Latin"); }
\p{Limbu} { setCurCharPropertyValue("Limbu"); }
\p{Linear B} { setCurCharPropertyValue("Linear B"); }
\p{Malayalam} { setCurCharPropertyValue("Malayalam"); }
\p{Mongolian} { setCurCharPropertyValue("Mongolian"); }
\p{Myanmar} { setCurCharPropertyValue("Myanmar"); }
\p{New Tai Lue} { setCurCharPropertyValue("New Tai Lue"); }
\p{Nko} { setCurCharPropertyValue("Nko"); }
\p{Ogham} { setCurCharPropertyValue("Ogham"); }
\p{Old Italic} { setCurCharPropertyValue("Old Italic"); }
\p{Old Persian} { setCurCharPropertyValue("Old Persian"); }
\p{Oriya} { setCurCharPropertyValue("Oriya"); }
\p{Osmanya} { setCurCharPropertyValue("Osmanya"); }
\p{Phags Pa} { setCurCharPropertyValue("Phags Pa"); }
\p{Phoenician} { setCurCharPropertyValue("Phoenician"); }
\p{Runic} { setCurCharPropertyValue("Runic"); }
\p{Shavian} { setCurCharPropertyValue("Shavian"); }
\p{Sinhala} { setCurCharPropertyValue("Sinhala"); }
\p{Syloti Nagri} { setCurCharPropertyValue("Syloti Nagri"); }
\p{Syriac} { setCurCharPropertyValue("Syriac"); }
\p{Tagalog} { setCurCharPropertyValue("Tagalog"); }
\p{Tagbanwa} { setCurCharPropertyValue("Tagbanwa"); }
\p{Tai Le} { setCurCharPropertyValue("Tai Le"); }
\p{Tamil} { setCurCharPropertyValue("Tamil"); }
\p{Telugu} { setCurCharPropertyValue("Telugu"); }
\p{Thaana} { setCurCharPropertyValue("Thaana"); }
\p{Thai} { setCurCharPropertyValue("Thai"); }
\p{Tibetan} { setCurCharPropertyValue("Tibetan"); }
\p{Tifinagh} { setCurCharPropertyValue("Tifinagh"); }
\p{Ugaritic} { setCurCharPropertyValue("Ugaritic"); }
\p{Unknown} { setCurCharPropertyValue("Unknown"); }
\p{Yi} { setCurCharPropertyValue("Yi"); }
<<EOF>> { printOutput(); return 1; }
