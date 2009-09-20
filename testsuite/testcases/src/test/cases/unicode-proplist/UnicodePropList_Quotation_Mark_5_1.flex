%%

%unicode 5.1
%public
%class UnicodePropList_Quotation_Mark_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Quotation_Mark} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
