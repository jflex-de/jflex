%%

%unicode 3.2
%public
%class UnicodePropList_Unified_Ideograph_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Unified_Ideograph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
