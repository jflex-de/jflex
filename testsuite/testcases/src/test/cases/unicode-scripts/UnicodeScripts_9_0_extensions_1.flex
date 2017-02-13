%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Adlam} { setCurCharPropertyValue("Script_Extensions:Adlam"); }
\p{Script_Extensions:Armenian} { setCurCharPropertyValue("Script_Extensions:Armenian"); }
\p{Script_Extensions:Bengali} { setCurCharPropertyValue("Script_Extensions:Bengali"); }
\p{Script_Extensions:Bopomofo} { setCurCharPropertyValue("Script_Extensions:Bopomofo"); }
\p{Script_Extensions:Buginese} { setCurCharPropertyValue("Script_Extensions:Buginese"); }
\p{Script_Extensions:Buhid} { setCurCharPropertyValue("Script_Extensions:Buhid"); }
\p{Script_Extensions:Coptic} { setCurCharPropertyValue("Script_Extensions:Coptic"); }
\p{Script_Extensions:Cypriot} { setCurCharPropertyValue("Script_Extensions:Cypriot"); }
\p{Script_Extensions:Cyrillic} { setCurCharPropertyValue("Script_Extensions:Cyrillic"); }
\p{Script_Extensions:Duployan} { setCurCharPropertyValue("Script_Extensions:Duployan"); }
\p{Script_Extensions:Greek} { setCurCharPropertyValue("Script_Extensions:Greek"); }
\p{Script_Extensions:Kaithi} { setCurCharPropertyValue("Script_Extensions:Kaithi"); }
\p{Script_Extensions:Kayah_Li} { setCurCharPropertyValue("Script_Extensions:Kayah_Li"); }
\p{Script_Extensions:Khojki} { setCurCharPropertyValue("Script_Extensions:Khojki"); }
\p{Script_Extensions:Mongolian} { setCurCharPropertyValue("Script_Extensions:Mongolian"); }
\p{Script_Extensions:Multani} { setCurCharPropertyValue("Script_Extensions:Multani"); }
\p{Script_Extensions:Tai_Le} { setCurCharPropertyValue("Script_Extensions:Tai_Le"); }
\p{Script_Extensions:Thaana} { setCurCharPropertyValue("Script_Extensions:Thaana"); }
[^] { }
