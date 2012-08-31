%%

%unicode 3.0
%public
%class UnicodePropList_Quotation_Mark_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Quotation Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
