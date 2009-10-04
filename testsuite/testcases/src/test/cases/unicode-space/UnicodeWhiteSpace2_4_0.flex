%%

%unicode 4.0
%public
%class UnicodeWhiteSpace2_4_0

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\s { setCurCharPropertyValue("Whitespace"); }
[^\s] { setCurCharPropertyValue("Not-Whitespace"); }
