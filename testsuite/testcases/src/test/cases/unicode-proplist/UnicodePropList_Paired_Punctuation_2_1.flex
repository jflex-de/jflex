%%

%unicode 2.1
%public
%class UnicodePropList_Paired_Punctuation_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Paired Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
