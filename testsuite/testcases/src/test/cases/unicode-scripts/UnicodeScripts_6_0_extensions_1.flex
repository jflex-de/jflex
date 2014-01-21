%%

%unicode 6.0
%public
%class UnicodeScripts_6_0_extensions_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Arabic} { setCurCharPropertyValue("Script_Extensions:Arabic"); }
\p{Script_Extensions:Armenian} { setCurCharPropertyValue("Script_Extensions:Armenian"); }
\p{Script_Extensions:Bengali} { setCurCharPropertyValue("Script_Extensions:Bengali"); }
\p{Script_Extensions:Bopomofo} { setCurCharPropertyValue("Script_Extensions:Bopomofo"); }
\p{Script_Extensions:Buhid} { setCurCharPropertyValue("Script_Extensions:Buhid"); }
\p{Script_Extensions:Mongolian} { setCurCharPropertyValue("Script_Extensions:Mongolian"); }
[^] { }
