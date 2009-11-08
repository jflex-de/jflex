%%

%unicode 4.1
%public
%class UnicodePropList_Hex_Digit_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
