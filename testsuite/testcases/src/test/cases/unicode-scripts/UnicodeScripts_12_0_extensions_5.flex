%%

%unicode 12.0
%public
%class UnicodeScripts_12_0_extensions_5

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Gujarati} { setCurCharPropertyValue("Script_Extensions:Gujarati"); }
\p{Script_Extensions:Katakana} { setCurCharPropertyValue("Script_Extensions:Katakana"); }
\p{Script_Extensions:Manichaean} { setCurCharPropertyValue("Script_Extensions:Manichaean"); }
[^] { }
