%%

%unicode 11.0
%public
%class UnicodePropList_Other_Grapheme_Extend_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Other_Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
