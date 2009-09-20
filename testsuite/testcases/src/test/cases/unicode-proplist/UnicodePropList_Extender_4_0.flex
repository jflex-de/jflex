%%

%unicode 4.0
%public
%class UnicodePropList_Extender_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Extender} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
