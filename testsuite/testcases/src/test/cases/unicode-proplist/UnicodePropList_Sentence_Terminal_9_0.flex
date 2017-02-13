%%

%unicode 9.0
%public
%class UnicodePropList_Sentence_Terminal_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Sentence_Terminal} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
