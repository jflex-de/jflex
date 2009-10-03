%%

%unicode 5.1
%public
%class UnicodeScriptAliases2_5_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%% 

\p{sc=arab} { setCurCharPropertyValue("Arabic"); }
\p{sc=armn} { setCurCharPropertyValue("Armenian"); }
\p{sc=bali} { setCurCharPropertyValue("Balinese"); }
\p{sc=beng} { setCurCharPropertyValue("Bengali"); }
\p{sc=bopo} { setCurCharPropertyValue("Bopomofo"); }
\p{sc=brai} { setCurCharPropertyValue("Braille"); }
\p{sc=bugi} { setCurCharPropertyValue("Buginese"); }
\p{sc=buhd} { setCurCharPropertyValue("Buhid"); }
\p{sc=cans} { setCurCharPropertyValue("Canadian Aboriginal"); }
\p{sc=cari} { setCurCharPropertyValue("Carian"); }
\p{sc=cham} { setCurCharPropertyValue("Cham"); }
\p{sc=cher} { setCurCharPropertyValue("Cherokee"); }
\p{sc=copt} { setCurCharPropertyValue("Coptic"); }
\p{sc=cprt} { setCurCharPropertyValue("Cypriot"); }
\p{sc=cyrl} { setCurCharPropertyValue("Cyrillic"); }
\p{sc=deva} { setCurCharPropertyValue("Devanagari"); }
\p{sc=dsrt} { setCurCharPropertyValue("Deseret"); }
\p{sc=ethi} { setCurCharPropertyValue("Ethiopic"); }
\p{sc=geor} { setCurCharPropertyValue("Georgian"); }
\p{sc=glag} { setCurCharPropertyValue("Glagolitic"); }
\p{sc=goth} { setCurCharPropertyValue("Gothic"); }
\p{sc=grek} { setCurCharPropertyValue("Greek"); }
\p{sc=gujr} { setCurCharPropertyValue("Gujarati"); }
\p{sc=guru} { setCurCharPropertyValue("Gurmukhi"); }
\p{sc=hang} { setCurCharPropertyValue("Hangul"); }
\p{sc=hani} { setCurCharPropertyValue("Han"); }
\p{sc=hano} { setCurCharPropertyValue("Hanunoo"); }
\p{sc=hebr} { setCurCharPropertyValue("Hebrew"); }
\p{sc=hira} { setCurCharPropertyValue("Hiragana"); }
//\p{sc=hrkt} { setCurCharPropertyValue("Katakana Or Hiragana"); }
\p{sc=ital} { setCurCharPropertyValue("Old Italic"); }
\p{sc=kali} { setCurCharPropertyValue("Kayah Li"); }
\p{sc=kana} { setCurCharPropertyValue("Katakana"); }
\p{sc=khar} { setCurCharPropertyValue("Kharoshthi"); }
\p{sc=khmr} { setCurCharPropertyValue("Khmer"); }
\p{sc=knda} { setCurCharPropertyValue("Kannada"); }
\p{sc=laoo} { setCurCharPropertyValue("Lao"); }
\p{sc=latn} { setCurCharPropertyValue("Latin"); }
\p{sc=lepc} { setCurCharPropertyValue("Lepcha"); }
\p{sc=limb} { setCurCharPropertyValue("Limbu"); }
\p{sc=linb} { setCurCharPropertyValue("Linearb"); }
\p{sc=lyci} { setCurCharPropertyValue("Lycian"); }
\p{sc=lydi} { setCurCharPropertyValue("Lydian"); }
\p{sc=mlym} { setCurCharPropertyValue("Malayalam"); }
\p{sc=mong} { setCurCharPropertyValue("Mongolian"); }
\p{sc=mymr} { setCurCharPropertyValue("Myanmar"); }
\p{sc=nkoo} { setCurCharPropertyValue("Nko"); }
\p{sc=ogam} { setCurCharPropertyValue("Ogham"); }
\p{sc=olck} { setCurCharPropertyValue("Ol Chiki"); }
\p{sc=orya} { setCurCharPropertyValue("Oriya"); }
\p{sc=osma} { setCurCharPropertyValue("Osmanya"); }
\p{sc=phag} { setCurCharPropertyValue("Phags Pa"); }
\p{sc=phnx} { setCurCharPropertyValue("Phoenician"); }
\p{sc=qaai} { setCurCharPropertyValue("Inherited"); }
\p{sc=rjng} { setCurCharPropertyValue("Rejang"); }
\p{sc=runr} { setCurCharPropertyValue("Runic"); }
\p{sc=saur} { setCurCharPropertyValue("Saurashtra"); }
\p{sc=shaw} { setCurCharPropertyValue("Shavian"); }
\p{sc=sinh} { setCurCharPropertyValue("Sinhala"); }
\p{sc=sund} { setCurCharPropertyValue("Sundanese"); }
\p{sc=sylo} { setCurCharPropertyValue("Syloti Nagri"); }
\p{sc=syrc} { setCurCharPropertyValue("Syriac"); }
\p{sc=tagb} { setCurCharPropertyValue("Tagbanwa"); }
\p{sc=tale} { setCurCharPropertyValue("Tai Le"); }
\p{sc=talu} { setCurCharPropertyValue("New Tai Lue"); }
\p{sc=taml} { setCurCharPropertyValue("Tamil"); }
\p{sc=telu} { setCurCharPropertyValue("Telugu"); }
\p{sc=tfng} { setCurCharPropertyValue("Tifinagh"); }
\p{sc=tglg} { setCurCharPropertyValue("Tagalog"); }
\p{sc=thaa} { setCurCharPropertyValue("Thaana"); }
\p{sc=thai} { setCurCharPropertyValue("Thai"); }
\p{sc=tibt} { setCurCharPropertyValue("Tibetan"); }
\p{sc=ugar} { setCurCharPropertyValue("Ugaritic"); }
\p{sc=vaii} { setCurCharPropertyValue("Vai"); }
\p{sc=xpeo} { setCurCharPropertyValue("Old Persian"); }
\p{sc=xsux} { setCurCharPropertyValue("Cuneiform"); }
\p{sc=yiii} { setCurCharPropertyValue("Yi"); }
\p{sc=zyyy} { setCurCharPropertyValue("Common"); }
\p{sc=zzzz} { setCurCharPropertyValue("Unknown"); }
<<EOF>> { printOutput(); return 1; }
