%%

%unicode 3.1
%public
%class UnicodePropList_Extender_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Extender} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
