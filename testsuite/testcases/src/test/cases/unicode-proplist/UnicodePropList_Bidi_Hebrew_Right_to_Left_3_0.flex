%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Hebrew_Right_to_Left_3_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Bidi: (Hebrew) Right-to-Left} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
