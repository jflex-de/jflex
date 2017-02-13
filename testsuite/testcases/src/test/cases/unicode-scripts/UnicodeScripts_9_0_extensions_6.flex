%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_6

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Kannada} { setCurCharPropertyValue("Script_Extensions:Kannada"); }
\p{Script_Extensions:Syriac} { setCurCharPropertyValue("Script_Extensions:Syriac"); }
\p{Script_Extensions:Yi} { setCurCharPropertyValue("Script_Extensions:Yi"); }
[^] { }
