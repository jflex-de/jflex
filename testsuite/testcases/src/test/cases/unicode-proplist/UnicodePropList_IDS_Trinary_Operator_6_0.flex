%%

%unicode 6.0
%public
%class UnicodePropList_IDS_Trinary_Operator_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{IDS_Trinary_Operator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
