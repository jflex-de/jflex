%%

%unicode 6.3
%public
%class UnicodeWhiteSpace2_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\s { setCurCharPropertyValue("Whitespace"); }
[^\s] { setCurCharPropertyValue("Not-Whitespace"); }
