%%

%unicode 5.0
%public
%class UnicodeDecimalDigit2_5_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\D { setCurCharPropertyValue("Not-Nd"); }
[^\D] { setCurCharPropertyValue("Nd"); }
