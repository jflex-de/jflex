%%

%unicode 2.1
%public
%class UnicodePropList_Hex_Digit_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hex Digit} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
