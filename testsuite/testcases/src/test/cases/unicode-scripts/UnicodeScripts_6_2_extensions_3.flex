%%

%unicode 6.2
%public
%class UnicodeScripts_6_2_extensions_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Gurmukhi} { setCurCharPropertyValue("Script_Extensions:Gurmukhi"); }
\p{Script_Extensions:Hangul} { setCurCharPropertyValue("Script_Extensions:Hangul"); }
\p{Script_Extensions:Latin} { setCurCharPropertyValue("Script_Extensions:Latin"); }
\p{Script_Extensions:Syriac} { setCurCharPropertyValue("Script_Extensions:Syriac"); }
\p{Script_Extensions:Tagalog} { setCurCharPropertyValue("Script_Extensions:Tagalog"); }
[^] { }
