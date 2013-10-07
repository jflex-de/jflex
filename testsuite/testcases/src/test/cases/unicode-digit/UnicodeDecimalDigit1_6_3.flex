%%

%unicode 6.3
%public
%class UnicodeDecimalDigit1_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:digit:] { setCurCharPropertyValue("Nd"); }
[^[:digit:]] { setCurCharPropertyValue("Not-Nd"); }
