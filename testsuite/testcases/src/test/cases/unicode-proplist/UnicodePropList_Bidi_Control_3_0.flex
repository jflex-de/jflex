%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Control_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
