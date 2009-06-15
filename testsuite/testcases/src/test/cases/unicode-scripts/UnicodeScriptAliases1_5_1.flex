%%

%unicode 5.1
%public
%class UnicodeScriptAliases1_5_1

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java

%% 

\p{arab} { setCurCharBlock("Arabic"); }
\p{armn} { setCurCharBlock("Armenian"); }
\p{bali} { setCurCharBlock("Balinese"); }
\p{beng} { setCurCharBlock("Bengali"); }
\p{bopo} { setCurCharBlock("Bopomofo"); }
\p{brai} { setCurCharBlock("Braille"); }
\p{bugi} { setCurCharBlock("Buginese"); }
\p{buhd} { setCurCharBlock("Buhid"); }
\p{cans} { setCurCharBlock("Canadian Aboriginal"); }
\p{cari} { setCurCharBlock("Carian"); }
\p{cham} { setCurCharBlock("Cham"); }
\p{cher} { setCurCharBlock("Cherokee"); }
\p{copt} { setCurCharBlock("Coptic"); }
\p{cprt} { setCurCharBlock("Cypriot"); }
\p{cyrl} { setCurCharBlock("Cyrillic"); }
\p{deva} { setCurCharBlock("Devanagari"); }
\p{dsrt} { setCurCharBlock("Deseret"); }
\p{ethi} { setCurCharBlock("Ethiopic"); }
\p{geor} { setCurCharBlock("Georgian"); }
\p{glag} { setCurCharBlock("Glagolitic"); }
\p{goth} { setCurCharBlock("Gothic"); }
\p{grek} { setCurCharBlock("Greek"); }
\p{gujr} { setCurCharBlock("Gujarati"); }
\p{guru} { setCurCharBlock("Gurmukhi"); }
\p{hang} { setCurCharBlock("Hangul"); }
\p{hani} { setCurCharBlock("Han"); }
\p{hano} { setCurCharBlock("Hanunoo"); }
\p{hebr} { setCurCharBlock("Hebrew"); }
\p{hira} { setCurCharBlock("Hiragana"); }
//\p{hrkt} { setCurCharBlock("Katakana Or Hiragana"); }
\p{ital} { setCurCharBlock("Old Italic"); }
\p{kali} { setCurCharBlock("Kayah Li"); }
\p{kana} { setCurCharBlock("Katakana"); }
\p{khar} { setCurCharBlock("Kharoshthi"); }
\p{khmr} { setCurCharBlock("Khmer"); }
\p{knda} { setCurCharBlock("Kannada"); }
\p{laoo} { setCurCharBlock("Lao"); }
\p{latn} { setCurCharBlock("Latin"); }
\p{lepc} { setCurCharBlock("Lepcha"); }
\p{limb} { setCurCharBlock("Limbu"); }
\p{linb} { setCurCharBlock("Linearb"); }
\p{lyci} { setCurCharBlock("Lycian"); }
\p{lydi} { setCurCharBlock("Lydian"); }
\p{mlym} { setCurCharBlock("Malayalam"); }
\p{mong} { setCurCharBlock("Mongolian"); }
\p{mymr} { setCurCharBlock("Myanmar"); }
\p{nkoo} { setCurCharBlock("Nko"); }
\p{ogam} { setCurCharBlock("Ogham"); }
\p{olck} { setCurCharBlock("Ol Chiki"); }
\p{orya} { setCurCharBlock("Oriya"); }
\p{osma} { setCurCharBlock("Osmanya"); }
\p{phag} { setCurCharBlock("Phags Pa"); }
\p{phnx} { setCurCharBlock("Phoenician"); }
\p{qaai} { setCurCharBlock("Inherited"); }
\p{rjng} { setCurCharBlock("Rejang"); }
\p{runr} { setCurCharBlock("Runic"); }
\p{saur} { setCurCharBlock("Saurashtra"); }
\p{shaw} { setCurCharBlock("Shavian"); }
\p{sinh} { setCurCharBlock("Sinhala"); }
\p{sund} { setCurCharBlock("Sundanese"); }
\p{sylo} { setCurCharBlock("Syloti Nagri"); }
\p{syrc} { setCurCharBlock("Syriac"); }
\p{tagb} { setCurCharBlock("Tagbanwa"); }
\p{tale} { setCurCharBlock("Tai Le"); }
\p{talu} { setCurCharBlock("New Tai Lue"); }
\p{taml} { setCurCharBlock("Tamil"); }
\p{telu} { setCurCharBlock("Telugu"); }
\p{tfng} { setCurCharBlock("Tifinagh"); }
\p{tglg} { setCurCharBlock("Tagalog"); }
\p{thaa} { setCurCharBlock("Thaana"); }
\p{thai} { setCurCharBlock("Thai"); }
\p{tibt} { setCurCharBlock("Tibetan"); }
\p{ugar} { setCurCharBlock("Ugaritic"); }
\p{vaii} { setCurCharBlock("Vai"); }
\p{xpeo} { setCurCharBlock("Old Persian"); }
\p{xsux} { setCurCharBlock("Cuneiform"); }
\p{yiii} { setCurCharBlock("Yi"); }
\p{zyyy} { setCurCharBlock("Common"); }
\p{zzzz} { setCurCharBlock("Unknown"); }
<<EOF>> { printOutput(); return 1; }
