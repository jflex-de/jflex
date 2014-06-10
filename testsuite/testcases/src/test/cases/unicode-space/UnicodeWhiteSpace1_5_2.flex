%%

%unicode 5.2
%public
%class UnicodeWhiteSpace1_5_2

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\S { setCurCharPropertyValue("Not-Whitespace"); }
[^\S] { setCurCharPropertyValue("Whitespace"); }
