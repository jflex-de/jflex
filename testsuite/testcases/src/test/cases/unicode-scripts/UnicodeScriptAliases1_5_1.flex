%%

%unicode 5.1
%public
%class UnicodeScriptAliases1_5_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%% 

\p{arab} { setCurCharPropertyValue("Arabic"); }
\p{armn} { setCurCharPropertyValue("Armenian"); }
\p{bali} { setCurCharPropertyValue("Balinese"); }
\p{beng} { setCurCharPropertyValue("Bengali"); }
\p{bopo} { setCurCharPropertyValue("Bopomofo"); }
\p{brai} { setCurCharPropertyValue("Braille"); }
\p{bugi} { setCurCharPropertyValue("Buginese"); }
\p{buhd} { setCurCharPropertyValue("Buhid"); }
\p{cans} { setCurCharPropertyValue("Canadian_Aboriginal"); }
\p{cari} { setCurCharPropertyValue("Carian"); }
\p{cham} { setCurCharPropertyValue("Cham"); }
\p{cher} { setCurCharPropertyValue("Cherokee"); }
\p{copt} { setCurCharPropertyValue("Coptic"); }
\p{cprt} { setCurCharPropertyValue("Cypriot"); }
\p{cyrl} { setCurCharPropertyValue("Cyrillic"); }
\p{deva} { setCurCharPropertyValue("Devanagari"); }
\p{dsrt} { setCurCharPropertyValue("Deseret"); }
\p{ethi} { setCurCharPropertyValue("Ethiopic"); }
\p{geor} { setCurCharPropertyValue("Georgian"); }
\p{glag} { setCurCharPropertyValue("Glagolitic"); }
\p{goth} { setCurCharPropertyValue("Gothic"); }
\p{grek} { setCurCharPropertyValue("Greek"); }
\p{gujr} { setCurCharPropertyValue("Gujarati"); }
\p{guru} { setCurCharPropertyValue("Gurmukhi"); }
\p{hang} { setCurCharPropertyValue("Hangul"); }
\p{hani} { setCurCharPropertyValue("Han"); }
\p{hano} { setCurCharPropertyValue("Hanunoo"); }
\p{hebr} { setCurCharPropertyValue("Hebrew"); }
\p{hira} { setCurCharPropertyValue("Hiragana"); }
//\p{hrkt} { setCurCharPropertyValue("Katakana_Or_Hiragana"); }
\p{ital} { setCurCharPropertyValue("Old_Italic"); }
\p{kali} { setCurCharPropertyValue("Kayah_Li"); }
\p{kana} { setCurCharPropertyValue("Katakana"); }
\p{khar} { setCurCharPropertyValue("Kharoshthi"); }
\p{khmr} { setCurCharPropertyValue("Khmer"); }
\p{knda} { setCurCharPropertyValue("Kannada"); }
\p{laoo} { setCurCharPropertyValue("Lao"); }
\p{latn} { setCurCharPropertyValue("Latin"); }
\p{lepc} { setCurCharPropertyValue("Lepcha"); }
\p{limb} { setCurCharPropertyValue("Limbu"); }
\p{linb} { setCurCharPropertyValue("Linear_B"); }
\p{lyci} { setCurCharPropertyValue("Lycian"); }
\p{lydi} { setCurCharPropertyValue("Lydian"); }
\p{mlym} { setCurCharPropertyValue("Malayalam"); }
\p{mong} { setCurCharPropertyValue("Mongolian"); }
\p{mymr} { setCurCharPropertyValue("Myanmar"); }
\p{nkoo} { setCurCharPropertyValue("Nko"); }
\p{ogam} { setCurCharPropertyValue("Ogham"); }
\p{olck} { setCurCharPropertyValue("Ol_Chiki"); }
\p{orya} { setCurCharPropertyValue("Oriya"); }
\p{osma} { setCurCharPropertyValue("Osmanya"); }
\p{phag} { setCurCharPropertyValue("Phags_Pa"); }
\p{phnx} { setCurCharPropertyValue("Phoenician"); }
\p{qaai} { setCurCharPropertyValue("Inherited"); }
\p{rjng} { setCurCharPropertyValue("Rejang"); }
\p{runr} { setCurCharPropertyValue("Runic"); }
\p{saur} { setCurCharPropertyValue("Saurashtra"); }
\p{shaw} { setCurCharPropertyValue("Shavian"); }
\p{sinh} { setCurCharPropertyValue("Sinhala"); }
\p{sund} { setCurCharPropertyValue("Sundanese"); }
\p{sylo} { setCurCharPropertyValue("Syloti_Nagri"); }
\p{syrc} { setCurCharPropertyValue("Syriac"); }
\p{tagb} { setCurCharPropertyValue("Tagbanwa"); }
\p{tale} { setCurCharPropertyValue("Tai_Le"); }
\p{talu} { setCurCharPropertyValue("New_Tai_Lue"); }
\p{taml} { setCurCharPropertyValue("Tamil"); }
\p{telu} { setCurCharPropertyValue("Telugu"); }
\p{tfng} { setCurCharPropertyValue("Tifinagh"); }
\p{tglg} { setCurCharPropertyValue("Tagalog"); }
\p{thaa} { setCurCharPropertyValue("Thaana"); }
\p{thai} { setCurCharPropertyValue("Thai"); }
\p{tibt} { setCurCharPropertyValue("Tibetan"); }
\p{ugar} { setCurCharPropertyValue("Ugaritic"); }
\p{vaii} { setCurCharPropertyValue("Vai"); }
\p{xpeo} { setCurCharPropertyValue("Old_Persian"); }
\p{xsux} { setCurCharPropertyValue("Cuneiform"); }
\p{yiii} { setCurCharPropertyValue("Yi"); }
\p{zyyy} { setCurCharPropertyValue("Common"); }
\p{zzzz} { setCurCharPropertyValue("Unknown"); }
<<EOF>> { printOutput(); return 1; }
