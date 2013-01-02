%%

%unicode 6.2
%public
%class UnicodePropList_Quotation_Mark_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Quotation_Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
