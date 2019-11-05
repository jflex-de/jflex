%%

%unicode 12.0
%public
%class UnicodeScripts_12_0_extensions_4

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Grantha} { setCurCharPropertyValue("Script_Extensions:Grantha"); }
\p{Script_Extensions:Hiragana} { setCurCharPropertyValue("Script_Extensions:Hiragana"); }
\p{Script_Extensions:Khojki} { setCurCharPropertyValue("Script_Extensions:Khojki"); }
\p{Script_Extensions:Mandaic} { setCurCharPropertyValue("Script_Extensions:Mandaic"); }
\p{Script_Extensions:Myanmar} { setCurCharPropertyValue("Script_Extensions:Myanmar"); }
\p{Script_Extensions:Tagbanwa} { setCurCharPropertyValue("Script_Extensions:Tagbanwa"); }
[^] { }
