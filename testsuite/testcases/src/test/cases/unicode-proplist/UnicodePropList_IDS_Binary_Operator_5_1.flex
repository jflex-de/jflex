%%

%unicode 5.1
%public
%class UnicodePropList_IDS_Binary_Operator_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{IDS_Binary_Operator} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
