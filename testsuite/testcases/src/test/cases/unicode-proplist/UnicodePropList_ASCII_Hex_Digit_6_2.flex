%%

%unicode 6.2
%public
%class UnicodePropList_ASCII_Hex_Digit_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ASCII_Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
