%%

%unicode 5.1
%public
%class UnicodeNotGeneralCategory

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Ll} { setCurCharPropertyValue("Ll"); }
\P{Ll} { setCurCharPropertyValue("Not-Ll"); }
