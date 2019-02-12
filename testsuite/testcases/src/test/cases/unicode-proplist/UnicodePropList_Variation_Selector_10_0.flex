%%

%unicode 10.0
%public
%class UnicodePropList_Variation_Selector_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Variation_Selector} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
