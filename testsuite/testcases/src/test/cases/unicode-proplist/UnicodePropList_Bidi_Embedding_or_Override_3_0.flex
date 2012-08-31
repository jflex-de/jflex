%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Embedding_or_Override_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Embedding or Override} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
