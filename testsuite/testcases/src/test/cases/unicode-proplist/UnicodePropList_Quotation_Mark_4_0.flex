%%

%unicode 4.0
%public
%class UnicodePropList_Quotation_Mark_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Quotation_Mark} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
