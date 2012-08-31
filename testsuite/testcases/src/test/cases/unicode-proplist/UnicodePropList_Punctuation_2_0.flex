%%

%unicode 2.0
%public
%class UnicodePropList_Punctuation_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
