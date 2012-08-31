%%

%unicode 6.1
%public
%class UnicodePropList_IDS_Binary_Operator_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{IDS_Binary_Operator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
