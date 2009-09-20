%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Eur_Num_Separator_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Eur Num Separator} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
