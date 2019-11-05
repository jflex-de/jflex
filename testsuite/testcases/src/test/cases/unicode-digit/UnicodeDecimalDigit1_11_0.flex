%%

%unicode 11.0
%public
%class UnicodeDecimalDigit1_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:digit:] { setCurCharPropertyValue("Nd"); }
[^[:digit:]] { setCurCharPropertyValue("Not-Nd"); }
