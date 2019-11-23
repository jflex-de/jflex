%%

%unicode 12.1
%public
%class UnicodePropList_Sentence_Terminal_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Sentence_Terminal} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
