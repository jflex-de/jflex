%%

%unicode 3.0
%public
%class UnicodePropList_Lowercase_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
