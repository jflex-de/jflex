%%

%unicode 6.0
%public
%class UnicodeScripts_6_0

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Arabic} { setCurCharPropertyValue("Arabic"); }
\p{Armenian} { setCurCharPropertyValue("Armenian"); }
\p{Balinese} { setCurCharPropertyValue("Balinese"); }
\p{Bamum} { setCurCharPropertyValue("Bamum"); }
\p{Batak} { setCurCharPropertyValue("Batak"); }
\p{Bengali} { setCurCharPropertyValue("Bengali"); }
\p{Bopomofo} { setCurCharPropertyValue("Bopomofo"); }
\p{Braille} { setCurCharPropertyValue("Braille"); }
\p{Buginese} { setCurCharPropertyValue("Buginese"); }
\p{Buhid} { setCurCharPropertyValue("Buhid"); }
\p{Canadian_Aboriginal} { setCurCharPropertyValue("Canadian_Aboriginal"); }
\p{Cham} { setCurCharPropertyValue("Cham"); }
\p{Cherokee} { setCurCharPropertyValue("Cherokee"); }
\p{Common} { setCurCharPropertyValue("Common"); }
\p{Coptic} { setCurCharPropertyValue("Coptic"); }
\p{Cyrillic} { setCurCharPropertyValue("Cyrillic"); }
\p{Devanagari} { setCurCharPropertyValue("Devanagari"); }
\p{Ethiopic} { setCurCharPropertyValue("Ethiopic"); }
\p{Georgian} { setCurCharPropertyValue("Georgian"); }
\p{Glagolitic} { setCurCharPropertyValue("Glagolitic"); }
\p{Greek} { setCurCharPropertyValue("Greek"); }
\p{Gujarati} { setCurCharPropertyValue("Gujarati"); }
\p{Gurmukhi} { setCurCharPropertyValue("Gurmukhi"); }
\p{Han} { setCurCharPropertyValue("Han"); }
\p{Hangul} { setCurCharPropertyValue("Hangul"); }
\p{Hanunoo} { setCurCharPropertyValue("Hanunoo"); }
\p{Hebrew} { setCurCharPropertyValue("Hebrew"); }
\p{Hiragana} { setCurCharPropertyValue("Hiragana"); }
\p{Inherited} { setCurCharPropertyValue("Inherited"); }
\p{Javanese} { setCurCharPropertyValue("Javanese"); }
\p{Kannada} { setCurCharPropertyValue("Kannada"); }
\p{Katakana} { setCurCharPropertyValue("Katakana"); }
\p{Kayah_Li} { setCurCharPropertyValue("Kayah_Li"); }
\p{Khmer} { setCurCharPropertyValue("Khmer"); }
\p{Lao} { setCurCharPropertyValue("Lao"); }
\p{Latin} { setCurCharPropertyValue("Latin"); }
\p{Lepcha} { setCurCharPropertyValue("Lepcha"); }
\p{Limbu} { setCurCharPropertyValue("Limbu"); }
\p{Lisu} { setCurCharPropertyValue("Lisu"); }
\p{Malayalam} { setCurCharPropertyValue("Malayalam"); }
\p{Mandaic} { setCurCharPropertyValue("Mandaic"); }
\p{Meetei_Mayek} { setCurCharPropertyValue("Meetei_Mayek"); }
\p{Mongolian} { setCurCharPropertyValue("Mongolian"); }
\p{Myanmar} { setCurCharPropertyValue("Myanmar"); }
\p{New_Tai_Lue} { setCurCharPropertyValue("New_Tai_Lue"); }
\p{Nko} { setCurCharPropertyValue("Nko"); }
\p{Ogham} { setCurCharPropertyValue("Ogham"); }
\p{Ol_Chiki} { setCurCharPropertyValue("Ol_Chiki"); }
\p{Oriya} { setCurCharPropertyValue("Oriya"); }
\p{Phags_Pa} { setCurCharPropertyValue("Phags_Pa"); }
\p{Rejang} { setCurCharPropertyValue("Rejang"); }
\p{Runic} { setCurCharPropertyValue("Runic"); }
\p{Samaritan} { setCurCharPropertyValue("Samaritan"); }
\p{Saurashtra} { setCurCharPropertyValue("Saurashtra"); }
\p{Sinhala} { setCurCharPropertyValue("Sinhala"); }
\p{Sundanese} { setCurCharPropertyValue("Sundanese"); }
\p{Syloti_Nagri} { setCurCharPropertyValue("Syloti_Nagri"); }
\p{Syriac} { setCurCharPropertyValue("Syriac"); }
\p{Tagalog} { setCurCharPropertyValue("Tagalog"); }
\p{Tagbanwa} { setCurCharPropertyValue("Tagbanwa"); }
\p{Tai_Le} { setCurCharPropertyValue("Tai_Le"); }
\p{Tai_Tham} { setCurCharPropertyValue("Tai_Tham"); }
\p{Tai_Viet} { setCurCharPropertyValue("Tai_Viet"); }
\p{Tamil} { setCurCharPropertyValue("Tamil"); }
\p{Telugu} { setCurCharPropertyValue("Telugu"); }
\p{Thaana} { setCurCharPropertyValue("Thaana"); }
\p{Thai} { setCurCharPropertyValue("Thai"); }
\p{Tibetan} { setCurCharPropertyValue("Tibetan"); }
\p{Tifinagh} { setCurCharPropertyValue("Tifinagh"); }
\p{Unknown} { setCurCharPropertyValue("Unknown"); }
\p{Vai} { setCurCharPropertyValue("Vai"); }
\p{Yi} { setCurCharPropertyValue("Yi"); }
