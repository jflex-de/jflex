%%

%unicode 12.1
%public
%class UnicodeScripts_12_1_extensions_15

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Oriya} { setCurCharPropertyValue("Script_Extensions:Oriya"); }
[^] { }
