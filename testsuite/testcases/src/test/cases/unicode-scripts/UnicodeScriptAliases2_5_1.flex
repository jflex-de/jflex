%%

%unicode 5.1
%public
%class UnicodeScriptAliases2_5_1

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java

%% 

\p{sc=arab} { setCurCharBlock("Arabic"); }
\p{sc=armn} { setCurCharBlock("Armenian"); }
\p{sc=bali} { setCurCharBlock("Balinese"); }
\p{sc=beng} { setCurCharBlock("Bengali"); }
\p{sc=bopo} { setCurCharBlock("Bopomofo"); }
\p{sc=brai} { setCurCharBlock("Braille"); }
\p{sc=bugi} { setCurCharBlock("Buginese"); }
\p{sc=buhd} { setCurCharBlock("Buhid"); }
\p{sc=cans} { setCurCharBlock("Canadian Aboriginal"); }
\p{sc=cari} { setCurCharBlock("Carian"); }
\p{sc=cham} { setCurCharBlock("Cham"); }
\p{sc=cher} { setCurCharBlock("Cherokee"); }
\p{sc=copt} { setCurCharBlock("Coptic"); }
\p{sc=cprt} { setCurCharBlock("Cypriot"); }
\p{sc=cyrl} { setCurCharBlock("Cyrillic"); }
\p{sc=deva} { setCurCharBlock("Devanagari"); }
\p{sc=dsrt} { setCurCharBlock("Deseret"); }
\p{sc=ethi} { setCurCharBlock("Ethiopic"); }
\p{sc=geor} { setCurCharBlock("Georgian"); }
\p{sc=glag} { setCurCharBlock("Glagolitic"); }
\p{sc=goth} { setCurCharBlock("Gothic"); }
\p{sc=grek} { setCurCharBlock("Greek"); }
\p{sc=gujr} { setCurCharBlock("Gujarati"); }
\p{sc=guru} { setCurCharBlock("Gurmukhi"); }
\p{sc=hang} { setCurCharBlock("Hangul"); }
\p{sc=hani} { setCurCharBlock("Han"); }
\p{sc=hano} { setCurCharBlock("Hanunoo"); }
\p{sc=hebr} { setCurCharBlock("Hebrew"); }
\p{sc=hira} { setCurCharBlock("Hiragana"); }
//\p{sc=hrkt} { setCurCharBlock("Katakana Or Hiragana"); }
\p{sc=ital} { setCurCharBlock("Old Italic"); }
\p{sc=kali} { setCurCharBlock("Kayah Li"); }
\p{sc=kana} { setCurCharBlock("Katakana"); }
\p{sc=khar} { setCurCharBlock("Kharoshthi"); }
\p{sc=khmr} { setCurCharBlock("Khmer"); }
\p{sc=knda} { setCurCharBlock("Kannada"); }
\p{sc=laoo} { setCurCharBlock("Lao"); }
\p{sc=latn} { setCurCharBlock("Latin"); }
\p{sc=lepc} { setCurCharBlock("Lepcha"); }
\p{sc=limb} { setCurCharBlock("Limbu"); }
\p{sc=linb} { setCurCharBlock("Linearb"); }
\p{sc=lyci} { setCurCharBlock("Lycian"); }
\p{sc=lydi} { setCurCharBlock("Lydian"); }
\p{sc=mlym} { setCurCharBlock("Malayalam"); }
\p{sc=mong} { setCurCharBlock("Mongolian"); }
\p{sc=mymr} { setCurCharBlock("Myanmar"); }
\p{sc=nkoo} { setCurCharBlock("Nko"); }
\p{sc=ogam} { setCurCharBlock("Ogham"); }
\p{sc=olck} { setCurCharBlock("Ol Chiki"); }
\p{sc=orya} { setCurCharBlock("Oriya"); }
\p{sc=osma} { setCurCharBlock("Osmanya"); }
\p{sc=phag} { setCurCharBlock("Phags Pa"); }
\p{sc=phnx} { setCurCharBlock("Phoenician"); }
\p{sc=qaai} { setCurCharBlock("Inherited"); }
\p{sc=rjng} { setCurCharBlock("Rejang"); }
\p{sc=runr} { setCurCharBlock("Runic"); }
\p{sc=saur} { setCurCharBlock("Saurashtra"); }
\p{sc=shaw} { setCurCharBlock("Shavian"); }
\p{sc=sinh} { setCurCharBlock("Sinhala"); }
\p{sc=sund} { setCurCharBlock("Sundanese"); }
\p{sc=sylo} { setCurCharBlock("Syloti Nagri"); }
\p{sc=syrc} { setCurCharBlock("Syriac"); }
\p{sc=tagb} { setCurCharBlock("Tagbanwa"); }
\p{sc=tale} { setCurCharBlock("Tai Le"); }
\p{sc=talu} { setCurCharBlock("New Tai Lue"); }
\p{sc=taml} { setCurCharBlock("Tamil"); }
\p{sc=telu} { setCurCharBlock("Telugu"); }
\p{sc=tfng} { setCurCharBlock("Tifinagh"); }
\p{sc=tglg} { setCurCharBlock("Tagalog"); }
\p{sc=thaa} { setCurCharBlock("Thaana"); }
\p{sc=thai} { setCurCharBlock("Thai"); }
\p{sc=tibt} { setCurCharBlock("Tibetan"); }
\p{sc=ugar} { setCurCharBlock("Ugaritic"); }
\p{sc=vaii} { setCurCharBlock("Vai"); }
\p{sc=xpeo} { setCurCharBlock("Old Persian"); }
\p{sc=xsux} { setCurCharBlock("Cuneiform"); }
\p{sc=yiii} { setCurCharBlock("Yi"); }
\p{sc=zyyy} { setCurCharBlock("Common"); }
\p{sc=zzzz} { setCurCharBlock("Unknown"); }
<<EOF>> { printOutput(); return 1; }
