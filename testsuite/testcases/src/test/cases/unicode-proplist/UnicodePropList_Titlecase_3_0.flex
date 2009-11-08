%%

%unicode 3.0
%public
%class UnicodePropList_Titlecase_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Titlecase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
