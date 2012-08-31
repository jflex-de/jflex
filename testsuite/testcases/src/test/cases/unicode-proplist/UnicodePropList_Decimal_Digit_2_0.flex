%%

%unicode 2.0
%public
%class UnicodePropList_Decimal_Digit_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Decimal Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
