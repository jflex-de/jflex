%%

%unicode 4.1
%public
%class UnicodePropList_Bidi_Control_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi_Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
