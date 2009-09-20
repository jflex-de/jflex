%%

%unicode 2.0
%public
%class UnicodePropList_Paired_Punctuation_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Paired Punctuation} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
