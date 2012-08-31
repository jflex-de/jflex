%%

%unicode 5.0
%public
%class UnicodePropList_Hex_Digit_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Hex_Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
