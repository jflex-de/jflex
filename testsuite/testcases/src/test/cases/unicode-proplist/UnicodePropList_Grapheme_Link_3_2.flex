%%

%unicode 3.2
%public
%class UnicodePropList_Grapheme_Link_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
