%%

%unicode 2.0
%public
%class UnicodePropList_Join_Control_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Join Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
