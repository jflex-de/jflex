%%

%unicode 12.1
%public
%class UnicodePropList_Unified_Ideograph_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Unified_Ideograph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
