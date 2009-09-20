%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Right_to_Left_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Right-to-Left} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
