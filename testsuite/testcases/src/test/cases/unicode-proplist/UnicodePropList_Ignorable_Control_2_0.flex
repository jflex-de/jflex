%%

%unicode 2.0
%public
%class UnicodePropList_Ignorable_Control_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Ignorable Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
