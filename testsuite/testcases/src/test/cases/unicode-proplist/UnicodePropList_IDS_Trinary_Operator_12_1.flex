%%

%unicode 12.1
%public
%class UnicodePropList_IDS_Trinary_Operator_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{IDS_Trinary_Operator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
