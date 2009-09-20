%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Left_to_Right_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Left-to-Right} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
