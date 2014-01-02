%%

%unicode 6.0
%public
%class UnicodeScripts_6_0_extensions_2

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Devanagari} { setCurCharPropertyValue("Script_Extensions:Devanagari"); }
\p{Script_Extensions:Georgian} { setCurCharPropertyValue("Script_Extensions:Georgian"); }
\p{Script_Extensions:Han} { setCurCharPropertyValue("Script_Extensions:Han"); }
\p{Script_Extensions:Hanunoo} { setCurCharPropertyValue("Script_Extensions:Hanunoo"); }
\p{Script_Extensions:Phags_Pa} { setCurCharPropertyValue("Script_Extensions:Phags_Pa"); }
\p{Script_Extensions:Syriac} { setCurCharPropertyValue("Script_Extensions:Syriac"); }
[^] { }
