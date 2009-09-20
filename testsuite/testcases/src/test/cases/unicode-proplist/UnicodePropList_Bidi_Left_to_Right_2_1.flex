%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Left_to_Right_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Left-to-Right} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
