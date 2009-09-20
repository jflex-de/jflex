%%

%unicode 4.1
%public
%class UnicodePropList_IDS_Trinary_Operator_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{IDS_Trinary_Operator} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
