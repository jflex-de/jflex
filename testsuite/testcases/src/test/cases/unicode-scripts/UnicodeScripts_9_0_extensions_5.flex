%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_5

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Gurmukhi} { setCurCharPropertyValue("Script_Extensions:Gurmukhi"); }
\p{Script_Extensions:Katakana} { setCurCharPropertyValue("Script_Extensions:Katakana"); }
\p{Script_Extensions:Psalter_Pahlavi} { setCurCharPropertyValue("Script_Extensions:Psalter_Pahlavi"); }
[^] { }
