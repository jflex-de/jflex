%%

%unicode 4.0
%public
%class UnicodePropList_IDS_Binary_Operator_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{IDS_Binary_Operator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
