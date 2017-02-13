%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_2

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Arabic} { setCurCharPropertyValue("Script_Extensions:Arabic"); }
\p{Script_Extensions:Chakma} { setCurCharPropertyValue("Script_Extensions:Chakma"); }
\p{Script_Extensions:Devanagari} { setCurCharPropertyValue("Script_Extensions:Devanagari"); }
\p{Script_Extensions:Georgian} { setCurCharPropertyValue("Script_Extensions:Georgian"); }
\p{Script_Extensions:Glagolitic} { setCurCharPropertyValue("Script_Extensions:Glagolitic"); }
\p{Script_Extensions:Han} { setCurCharPropertyValue("Script_Extensions:Han"); }
\p{Script_Extensions:Hanunoo} { setCurCharPropertyValue("Script_Extensions:Hanunoo"); }
\p{Script_Extensions:Javanese} { setCurCharPropertyValue("Script_Extensions:Javanese"); }
\p{Script_Extensions:Linear_A} { setCurCharPropertyValue("Script_Extensions:Linear_A"); }
\p{Script_Extensions:Old_Permic} { setCurCharPropertyValue("Script_Extensions:Old_Permic"); }
\p{Script_Extensions:Phags_Pa} { setCurCharPropertyValue("Script_Extensions:Phags_Pa"); }
[^] { }
