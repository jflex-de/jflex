%%

%unicode 3.1
%public
%class UnicodePropList_Join_Control_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Join_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
