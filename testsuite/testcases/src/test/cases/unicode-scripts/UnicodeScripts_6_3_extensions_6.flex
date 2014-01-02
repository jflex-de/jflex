%%

%unicode 6.3
%public
%class UnicodeScripts_6_3_extensions_6

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Yi} { setCurCharPropertyValue("Script_Extensions:Yi"); }
[^] { }
