%%

%unicode 8.0
%public
%class UnicodePropList_Bidi_Control_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Bidi_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
