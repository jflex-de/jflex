%%

%unicode 6.3
%public
%class UnicodeScripts_6_3_extensions_2

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Chakma} { setCurCharPropertyValue("Script_Extensions:Chakma"); }
\p{Script_Extensions:Devanagari} { setCurCharPropertyValue("Script_Extensions:Devanagari"); }
\p{Script_Extensions:Georgian} { setCurCharPropertyValue("Script_Extensions:Georgian"); }
\p{Script_Extensions:Han} { setCurCharPropertyValue("Script_Extensions:Han"); }
\p{Script_Extensions:Hanunoo} { setCurCharPropertyValue("Script_Extensions:Hanunoo"); }
\p{Script_Extensions:Javanese} { setCurCharPropertyValue("Script_Extensions:Javanese"); }
\p{Script_Extensions:Mandaic} { setCurCharPropertyValue("Script_Extensions:Mandaic"); }
\p{Script_Extensions:Phags_Pa} { setCurCharPropertyValue("Script_Extensions:Phags_Pa"); }
\p{Script_Extensions:Thaana} { setCurCharPropertyValue("Script_Extensions:Thaana"); }
[^] { }
