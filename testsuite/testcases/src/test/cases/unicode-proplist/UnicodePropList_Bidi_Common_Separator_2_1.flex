%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Common_Separator_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Common Separator} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
