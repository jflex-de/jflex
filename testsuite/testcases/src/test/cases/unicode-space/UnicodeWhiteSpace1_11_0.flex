%%

%unicode 11.0
%public
%class UnicodeWhiteSpace1_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\S { setCurCharPropertyValue("Not-Whitespace"); }
[^\S] { setCurCharPropertyValue("Whitespace"); }
