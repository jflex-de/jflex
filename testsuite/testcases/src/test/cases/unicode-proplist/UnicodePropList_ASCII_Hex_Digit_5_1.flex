%%

%unicode 5.1
%public
%class UnicodePropList_ASCII_Hex_Digit_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ASCII_Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
