%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Eur_Num_Terminator_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Eur Num Terminator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
