%%

%unicode 6.1
%public
%class UnicodePropList_Join_Control_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Join_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
