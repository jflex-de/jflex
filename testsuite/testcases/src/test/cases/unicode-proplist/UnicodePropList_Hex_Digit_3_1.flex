%%

%unicode 3.1
%public
%class UnicodePropList_Hex_Digit_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hex_Digit} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
