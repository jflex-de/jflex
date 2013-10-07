%%

%unicode 6.3
%public
%class UnicodePropList_Hex_Digit_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
