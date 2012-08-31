%%

%unicode 3.0
%public
%class UnicodePropList_Join_Control_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Join Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
