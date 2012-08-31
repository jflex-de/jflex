%%

%unicode 3.1
%public
%class UnicodePropList_Bidi_Control_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
