%%

%unicode 5.0
%public
%class UnicodePropList_Dash_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
