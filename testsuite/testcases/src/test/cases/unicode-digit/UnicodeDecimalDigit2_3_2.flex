%%

%unicode 3.2
%public
%class UnicodeDecimalDigit2_3_2

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\D { setCurCharPropertyValue("Not-Nd"); }
[^\D] { setCurCharPropertyValue("Nd"); }
