%%

%unicode 3.2
%public
%class UnicodePropList_Quotation_Mark_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Quotation_Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
