%%

%unicode 6.0
%public
%class UnicodePropList_Bidi_Control_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
