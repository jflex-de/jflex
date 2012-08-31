%%

%unicode 5.2
%public
%class UnicodePropList_Join_Control_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Join_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
