%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Eur_Num_Separator_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Eur Num Separator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
