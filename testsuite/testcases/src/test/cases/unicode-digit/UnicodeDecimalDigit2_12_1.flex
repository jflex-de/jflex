%%

%unicode 12.1
%public
%class UnicodeDecimalDigit2_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\D { setCurCharPropertyValue("Not-Nd"); }
[^\D] { setCurCharPropertyValue("Nd"); }
