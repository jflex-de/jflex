%%

%unicode 2.0
%public
%class UnicodePropList_Bidi_Segment_Separator_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Segment Separator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
