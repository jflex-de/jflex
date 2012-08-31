%%

%unicode 4.1
%public
%class UnicodePropList_Other_Grapheme_Extend_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
