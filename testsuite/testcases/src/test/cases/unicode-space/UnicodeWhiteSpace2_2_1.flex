%%

%unicode 2.1
%public
%class UnicodeWhiteSpace2_2_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\s { setCurCharPropertyValue("Whitespace"); }
[^\s] { setCurCharPropertyValue("Not-Whitespace"); }
