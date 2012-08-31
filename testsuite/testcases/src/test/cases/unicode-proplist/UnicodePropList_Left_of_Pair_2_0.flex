%%

%unicode 2.0
%public
%class UnicodePropList_Left_of_Pair_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Left of Pair} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
