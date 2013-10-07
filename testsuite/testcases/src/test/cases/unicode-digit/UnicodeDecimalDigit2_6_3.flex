%%

%unicode 6.3
%public
%class UnicodeDecimalDigit2_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\D { setCurCharPropertyValue("Not-Nd"); }
[^\D] { setCurCharPropertyValue("Nd"); }
