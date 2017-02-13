%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_8

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Limbu} { setCurCharPropertyValue("Script_Extensions:Limbu"); }
\p{Script_Extensions:Sharada} { setCurCharPropertyValue("Script_Extensions:Sharada"); }
[^] { }
