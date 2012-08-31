%%

%unicode 2.0
%public
%class UnicodePropList_Bidi_Whitespace_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Whitespace} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
