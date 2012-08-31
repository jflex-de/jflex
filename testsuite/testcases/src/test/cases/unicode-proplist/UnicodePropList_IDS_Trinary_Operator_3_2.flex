%%

%unicode 3.2
%public
%class UnicodePropList_IDS_Trinary_Operator_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{IDS_Trinary_Operator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
