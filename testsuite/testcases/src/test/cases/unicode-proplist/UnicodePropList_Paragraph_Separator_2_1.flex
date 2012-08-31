%%

%unicode 2.1
%public
%class UnicodePropList_Paragraph_Separator_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Paragraph Separator} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
