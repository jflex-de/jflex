%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Non_spacing_Mark_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Non-spacing Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
