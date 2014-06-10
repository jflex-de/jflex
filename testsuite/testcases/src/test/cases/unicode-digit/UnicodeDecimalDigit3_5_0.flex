%%

%unicode 5.0
%public
%class UnicodeDecimalDigit3_5_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\d { setCurCharPropertyValue("Nd"); }
[^\d] { setCurCharPropertyValue("Not-Nd"); }
