%%

%unicode 2.0
%public
%class UnicodePropList_Numeric_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Numeric} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
