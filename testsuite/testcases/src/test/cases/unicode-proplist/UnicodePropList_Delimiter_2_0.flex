%%

%unicode 2.0
%public
%class UnicodePropList_Delimiter_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Delimiter} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
