%%

%unicode 3.0
%public
%class UnicodePropList_Decimal_Digit_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Decimal Digit} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
