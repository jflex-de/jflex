%%

%unicode 5.1
%public
%class UnicodeWhiteSpace1_5_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\S { setCurCharPropertyValue("Not-Whitespace"); }
[^\S] { setCurCharPropertyValue("Whitespace"); }