%%

%unicode 5.0
%public
%class UnicodePropList_Bidi_Control_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi_Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
