%%

%unicode 5.2
%public
%class UnicodePropList_Bidi_Control_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi_Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
