%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Eur_Num_Terminator_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Eur Num Terminator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
