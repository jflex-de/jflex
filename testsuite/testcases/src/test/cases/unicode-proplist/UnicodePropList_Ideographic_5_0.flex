%%

%unicode 5.0
%public
%class UnicodePropList_Ideographic_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
