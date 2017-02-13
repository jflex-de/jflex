%%

%unicode 9.0
%public
%class UnicodeScripts_9_0_extensions_3

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Grantha} { setCurCharPropertyValue("Script_Extensions:Grantha"); }
\p{Script_Extensions:Hangul} { setCurCharPropertyValue("Script_Extensions:Hangul"); }
\p{Script_Extensions:Linear_B} { setCurCharPropertyValue("Script_Extensions:Linear_B"); }
\p{Script_Extensions:Mandaic} { setCurCharPropertyValue("Script_Extensions:Mandaic"); }
\p{Script_Extensions:Modi} { setCurCharPropertyValue("Script_Extensions:Modi"); }
\p{Script_Extensions:Myanmar} { setCurCharPropertyValue("Script_Extensions:Myanmar"); }
\p{Script_Extensions:Tagalog} { setCurCharPropertyValue("Script_Extensions:Tagalog"); }
[^] { }
