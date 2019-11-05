%%

%unicode 11.0
%public
%class UnicodeScripts_11_0_extensions_3

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Dogra} { setCurCharPropertyValue("Script_Extensions:Dogra"); }
\p{Script_Extensions:Hangul} { setCurCharPropertyValue("Script_Extensions:Hangul"); }
\p{Script_Extensions:Hanifi_Rohingya} { setCurCharPropertyValue("Script_Extensions:Hanifi_Rohingya"); }
\p{Script_Extensions:Latin} { setCurCharPropertyValue("Script_Extensions:Latin"); }
\p{Script_Extensions:Linear_B} { setCurCharPropertyValue("Script_Extensions:Linear_B"); }
\p{Script_Extensions:Tagalog} { setCurCharPropertyValue("Script_Extensions:Tagalog"); }
[^] { }
