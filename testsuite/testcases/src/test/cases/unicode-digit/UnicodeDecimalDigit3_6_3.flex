%%

%unicode 6.3
%public
%class UnicodeDecimalDigit3_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\d { setCurCharPropertyValue("Nd"); }
[^\d] { setCurCharPropertyValue("Not-Nd"); }
