%%

%unicode 4.0
%public
%class UnicodePropList_Other_ID_Start_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_ID_Start} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
