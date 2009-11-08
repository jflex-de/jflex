%%

%unicode 5.2
%public
%class UnicodePropList_Variation_Selector_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Variation_Selector} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
