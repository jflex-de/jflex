%%

%unicode 3.2
%public
%class UnicodePropList_ASCII_Hex_Digit_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ASCII_Hex_Digit} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
