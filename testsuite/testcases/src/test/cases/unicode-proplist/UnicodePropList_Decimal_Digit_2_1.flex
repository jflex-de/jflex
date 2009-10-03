%%

%unicode 2.1
%public
%class UnicodePropList_Decimal_Digit_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Decimal Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
