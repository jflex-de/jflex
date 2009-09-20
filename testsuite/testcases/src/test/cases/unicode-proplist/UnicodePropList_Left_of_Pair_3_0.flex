%%

%unicode 3.0
%public
%class UnicodePropList_Left_of_Pair_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Left of Pair} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
