%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Block_Separator_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Bidi: Block Separator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
