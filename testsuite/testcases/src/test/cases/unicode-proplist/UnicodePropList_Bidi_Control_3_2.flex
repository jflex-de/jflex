%%

%unicode 3.2
%public
%class UnicodePropList_Bidi_Control_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
