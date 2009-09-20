%%

%unicode 5.1
%public
%class UnicodePropList_Join_Control_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Join_Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
