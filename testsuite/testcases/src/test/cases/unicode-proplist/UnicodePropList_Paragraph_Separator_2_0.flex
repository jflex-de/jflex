%%

%unicode 2.0
%public
%class UnicodePropList_Paragraph_Separator_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Paragraph Separator} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
