%%

%unicode 11.0
%public
%class UnicodeScripts_11_0_extensions_6

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Script_Extensions:Gunjala_Gondi} { setCurCharPropertyValue("Script_Extensions:Gunjala_Gondi"); }
\p{Script_Extensions:Modi} { setCurCharPropertyValue("Script_Extensions:Modi"); }
\p{Script_Extensions:Psalter_Pahlavi} { setCurCharPropertyValue("Script_Extensions:Psalter_Pahlavi"); }
\p{Script_Extensions:Sharada} { setCurCharPropertyValue("Script_Extensions:Sharada"); }
\p{Script_Extensions:Yi} { setCurCharPropertyValue("Script_Extensions:Yi"); }
[^] { }
