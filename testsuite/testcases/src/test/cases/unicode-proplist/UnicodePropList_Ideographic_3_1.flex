%%

%unicode 3.1
%public
%class UnicodePropList_Ideographic_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
