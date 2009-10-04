%%

%unicode 5.1
%public
%class UnicodeWhiteSpace1_5_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\S { setCurCharPropertyValue("Not-Whitespace"); }
[^\S] { setCurCharPropertyValue("Whitespace"); }
