%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Other_Neutral_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Other Neutral} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
