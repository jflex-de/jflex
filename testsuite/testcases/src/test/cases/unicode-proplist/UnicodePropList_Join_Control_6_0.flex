%%

%unicode 6.0
%public
%class UnicodePropList_Join_Control_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Join_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
