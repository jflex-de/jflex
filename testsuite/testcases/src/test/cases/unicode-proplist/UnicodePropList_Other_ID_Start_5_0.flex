%%

%unicode 5.0
%public
%class UnicodePropList_Other_ID_Start_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_ID_Start} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
