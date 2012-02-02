%%

%unicode 6.1
%public
%class UnicodePropList_Other_Grapheme_Extend_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
