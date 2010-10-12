%%

%unicode 6.0
%public
%class UnicodePropList_Unified_Ideograph_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Unified_Ideograph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
