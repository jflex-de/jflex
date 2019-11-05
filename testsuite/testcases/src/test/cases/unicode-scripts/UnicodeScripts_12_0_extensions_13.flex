%%

%unicode 12.0
%public
%class UnicodeScripts_12_0_extensions_13

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Masaram_Gondi} { setCurCharPropertyValue("Script_Extensions:Masaram_Gondi"); }
[^] { }
