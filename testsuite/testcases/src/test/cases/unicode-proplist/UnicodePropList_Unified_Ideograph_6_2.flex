%%

%unicode 6.2
%public
%class UnicodePropList_Unified_Ideograph_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Unified_Ideograph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
