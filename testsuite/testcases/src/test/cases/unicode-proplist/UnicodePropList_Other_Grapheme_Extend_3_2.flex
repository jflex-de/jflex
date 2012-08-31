%%

%unicode 3.2
%public
%class UnicodePropList_Other_Grapheme_Extend_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
