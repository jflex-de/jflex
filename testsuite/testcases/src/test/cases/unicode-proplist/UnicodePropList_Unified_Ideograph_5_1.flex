%%

%unicode 5.1
%public
%class UnicodePropList_Unified_Ideograph_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Unified_Ideograph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
