%%

%unicode 3.1
%public
%class UnicodePropList_Quotation_Mark_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Quotation_Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
