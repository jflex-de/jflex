%%

%unicode 4.1
%public
%class UnicodePropList_Variation_Selector_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Variation_Selector} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
