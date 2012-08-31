%%

%unicode 6.1
%public
%class UnicodePropList_Hex_Digit_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
