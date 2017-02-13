%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_4

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Gujarati} { setCurCharPropertyValue("Script_Extensions:Gujarati"); }
\p{Script_Extensions:Hiragana} { setCurCharPropertyValue("Script_Extensions:Hiragana"); }
\p{Script_Extensions:Manichaean} { setCurCharPropertyValue("Script_Extensions:Manichaean"); }
\p{Script_Extensions:Tagbanwa} { setCurCharPropertyValue("Script_Extensions:Tagbanwa"); }
[^] { }
