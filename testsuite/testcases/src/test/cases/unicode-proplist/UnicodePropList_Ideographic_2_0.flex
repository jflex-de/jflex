%%

%unicode 2.0
%public
%class UnicodePropList_Ideographic_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
