%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Left_to_Right_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Left-to-Right} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
