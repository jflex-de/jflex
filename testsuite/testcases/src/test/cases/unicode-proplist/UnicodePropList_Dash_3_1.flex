%%

%unicode 3.1
%public
%class UnicodePropList_Dash_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
