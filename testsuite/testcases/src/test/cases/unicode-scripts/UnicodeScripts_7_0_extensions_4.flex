%%

%unicode 7.0
%public
%class UnicodeScripts_7_0_extensions_4

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Gujarati} { setCurCharPropertyValue("Script_Extensions:Gujarati"); }
\p{Script_Extensions:Hiragana} { setCurCharPropertyValue("Script_Extensions:Hiragana"); }
\p{Script_Extensions:Myanmar} { setCurCharPropertyValue("Script_Extensions:Myanmar"); }
\p{Script_Extensions:Psalter_Pahlavi} { setCurCharPropertyValue("Script_Extensions:Psalter_Pahlavi"); }
\p{Script_Extensions:Tagbanwa} { setCurCharPropertyValue("Script_Extensions:Tagbanwa"); }
[^] { }
