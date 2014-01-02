%%

%unicode 6.3
%public
%class UnicodeScripts_6_3_extensions_5

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Katakana} { setCurCharPropertyValue("Script_Extensions:Katakana"); }
\p{Script_Extensions:Takri} { setCurCharPropertyValue("Script_Extensions:Takri"); }
[^] { }
