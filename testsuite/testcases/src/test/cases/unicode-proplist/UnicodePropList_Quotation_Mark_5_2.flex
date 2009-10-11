%%

%unicode 5.2
%public
%class UnicodePropList_Quotation_Mark_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Quotation_Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
