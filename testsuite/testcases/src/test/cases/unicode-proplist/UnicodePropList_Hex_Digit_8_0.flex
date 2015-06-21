%%

%unicode 8.0
%public
%class UnicodePropList_Hex_Digit_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
