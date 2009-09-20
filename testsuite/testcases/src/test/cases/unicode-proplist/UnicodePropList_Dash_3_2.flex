%%

%unicode 3.2
%public
%class UnicodePropList_Dash_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
