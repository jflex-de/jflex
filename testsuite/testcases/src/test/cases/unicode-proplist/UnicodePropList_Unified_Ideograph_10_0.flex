%%

%unicode 10.0
%public
%class UnicodePropList_Unified_Ideograph_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Unified_Ideograph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
