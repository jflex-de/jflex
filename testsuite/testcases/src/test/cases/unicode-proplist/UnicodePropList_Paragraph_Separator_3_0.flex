%%

%unicode 3.0
%public
%class UnicodePropList_Paragraph_Separator_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Paragraph Separator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
