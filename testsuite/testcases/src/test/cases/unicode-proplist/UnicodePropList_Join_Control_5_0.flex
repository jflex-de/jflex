%%

%unicode 5.0
%public
%class UnicodePropList_Join_Control_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Join_Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
