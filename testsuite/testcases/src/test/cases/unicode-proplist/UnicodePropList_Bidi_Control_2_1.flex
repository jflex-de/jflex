%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Control_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
