%%

%unicode 5.0
%public
%class UnicodePropList_IDS_Trinary_Operator_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{IDS_Trinary_Operator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
