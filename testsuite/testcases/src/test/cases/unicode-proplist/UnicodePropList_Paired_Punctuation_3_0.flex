%%

%unicode 3.0
%public
%class UnicodePropList_Paired_Punctuation_3_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Paired Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
