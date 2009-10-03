%%

%unicode 2.0
%public
%class UnicodePropList_Hex_Digit_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hex Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
