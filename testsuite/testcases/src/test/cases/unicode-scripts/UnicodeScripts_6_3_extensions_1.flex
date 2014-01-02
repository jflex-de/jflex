%%

%unicode 6.3
%public
%class UnicodeScripts_6_3_extensions_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Arabic} { setCurCharPropertyValue("Script_Extensions:Arabic"); }
\p{Script_Extensions:Armenian} { setCurCharPropertyValue("Script_Extensions:Armenian"); }
\p{Script_Extensions:Bengali} { setCurCharPropertyValue("Script_Extensions:Bengali"); }
\p{Script_Extensions:Bopomofo} { setCurCharPropertyValue("Script_Extensions:Bopomofo"); }
\p{Script_Extensions:Buginese} { setCurCharPropertyValue("Script_Extensions:Buginese"); }
\p{Script_Extensions:Buhid} { setCurCharPropertyValue("Script_Extensions:Buhid"); }
\p{Script_Extensions:Cyrillic} { setCurCharPropertyValue("Script_Extensions:Cyrillic"); }
\p{Script_Extensions:Greek} { setCurCharPropertyValue("Script_Extensions:Greek"); }
\p{Script_Extensions:Gujarati} { setCurCharPropertyValue("Script_Extensions:Gujarati"); }
\p{Script_Extensions:Mongolian} { setCurCharPropertyValue("Script_Extensions:Mongolian"); }
\p{Script_Extensions:Myanmar} { setCurCharPropertyValue("Script_Extensions:Myanmar"); }
[^] { }
