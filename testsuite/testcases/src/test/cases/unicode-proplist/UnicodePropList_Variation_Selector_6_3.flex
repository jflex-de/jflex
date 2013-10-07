%%

%unicode 6.3
%public
%class UnicodePropList_Variation_Selector_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Variation_Selector} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
