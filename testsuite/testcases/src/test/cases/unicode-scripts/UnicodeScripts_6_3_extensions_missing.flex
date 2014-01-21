%%

%unicode 6.3
%public
%class UnicodeScripts_6_3_extensions_missing

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Balinese} { setCurCharPropertyValue("Script_Extensions:Balinese"); }
\p{Script_Extensions:Bamum} { setCurCharPropertyValue("Script_Extensions:Bamum"); }
\p{Script_Extensions:Batak} { setCurCharPropertyValue("Script_Extensions:Batak"); }
\p{Script_Extensions:Braille} { setCurCharPropertyValue("Script_Extensions:Braille"); }
\p{Script_Extensions:Canadian_Aboriginal} { setCurCharPropertyValue("Script_Extensions:Canadian_Aboriginal"); }
\p{Script_Extensions:Cham} { setCurCharPropertyValue("Script_Extensions:Cham"); }
\p{Script_Extensions:Cherokee} { setCurCharPropertyValue("Script_Extensions:Cherokee"); }
\p{Script_Extensions:Common} { setCurCharPropertyValue("Script_Extensions:Common"); }
\p{Script_Extensions:Coptic} { setCurCharPropertyValue("Script_Extensions:Coptic"); }
\p{Script_Extensions:Ethiopic} { setCurCharPropertyValue("Script_Extensions:Ethiopic"); }
\p{Script_Extensions:Glagolitic} { setCurCharPropertyValue("Script_Extensions:Glagolitic"); }
\p{Script_Extensions:Hebrew} { setCurCharPropertyValue("Script_Extensions:Hebrew"); }
\p{Script_Extensions:Inherited} { setCurCharPropertyValue("Script_Extensions:Inherited"); }
\p{Script_Extensions:Kannada} { setCurCharPropertyValue("Script_Extensions:Kannada"); }
\p{Script_Extensions:Kayah_Li} { setCurCharPropertyValue("Script_Extensions:Kayah_Li"); }
\p{Script_Extensions:Khmer} { setCurCharPropertyValue("Script_Extensions:Khmer"); }
\p{Script_Extensions:Lao} { setCurCharPropertyValue("Script_Extensions:Lao"); }
\p{Script_Extensions:Lepcha} { setCurCharPropertyValue("Script_Extensions:Lepcha"); }
\p{Script_Extensions:Limbu} { setCurCharPropertyValue("Script_Extensions:Limbu"); }
\p{Script_Extensions:Lisu} { setCurCharPropertyValue("Script_Extensions:Lisu"); }
\p{Script_Extensions:Malayalam} { setCurCharPropertyValue("Script_Extensions:Malayalam"); }
\p{Script_Extensions:Meetei_Mayek} { setCurCharPropertyValue("Script_Extensions:Meetei_Mayek"); }
\p{Script_Extensions:New_Tai_Lue} { setCurCharPropertyValue("Script_Extensions:New_Tai_Lue"); }
\p{Script_Extensions:Nko} { setCurCharPropertyValue("Script_Extensions:Nko"); }
\p{Script_Extensions:Ogham} { setCurCharPropertyValue("Script_Extensions:Ogham"); }
\p{Script_Extensions:Ol_Chiki} { setCurCharPropertyValue("Script_Extensions:Ol_Chiki"); }
\p{Script_Extensions:Rejang} { setCurCharPropertyValue("Script_Extensions:Rejang"); }
\p{Script_Extensions:Runic} { setCurCharPropertyValue("Script_Extensions:Runic"); }
\p{Script_Extensions:Samaritan} { setCurCharPropertyValue("Script_Extensions:Samaritan"); }
\p{Script_Extensions:Saurashtra} { setCurCharPropertyValue("Script_Extensions:Saurashtra"); }
\p{Script_Extensions:Sinhala} { setCurCharPropertyValue("Script_Extensions:Sinhala"); }
\p{Script_Extensions:Sundanese} { setCurCharPropertyValue("Script_Extensions:Sundanese"); }
\p{Script_Extensions:Tai_Tham} { setCurCharPropertyValue("Script_Extensions:Tai_Tham"); }
\p{Script_Extensions:Tai_Viet} { setCurCharPropertyValue("Script_Extensions:Tai_Viet"); }
\p{Script_Extensions:Tamil} { setCurCharPropertyValue("Script_Extensions:Tamil"); }
\p{Script_Extensions:Telugu} { setCurCharPropertyValue("Script_Extensions:Telugu"); }
\p{Script_Extensions:Thai} { setCurCharPropertyValue("Script_Extensions:Thai"); }
\p{Script_Extensions:Tibetan} { setCurCharPropertyValue("Script_Extensions:Tibetan"); }
\p{Script_Extensions:Tifinagh} { setCurCharPropertyValue("Script_Extensions:Tifinagh"); }
\p{Script_Extensions:Unknown} { setCurCharPropertyValue("Script_Extensions:Unknown"); }
\p{Script_Extensions:Vai} { setCurCharPropertyValue("Script_Extensions:Vai"); }
[^] { }
