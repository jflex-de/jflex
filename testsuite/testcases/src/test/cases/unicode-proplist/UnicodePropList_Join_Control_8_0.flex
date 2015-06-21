%%

%unicode 8.0
%public
%class UnicodePropList_Join_Control_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Join_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
