%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Arabic_Right_to_Left_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Arabic Right-to-Left} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
