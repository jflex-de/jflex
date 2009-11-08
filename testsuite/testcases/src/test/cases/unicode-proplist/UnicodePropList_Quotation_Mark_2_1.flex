%%

%unicode 2.1
%public
%class UnicodePropList_Quotation_Mark_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Quotation Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
