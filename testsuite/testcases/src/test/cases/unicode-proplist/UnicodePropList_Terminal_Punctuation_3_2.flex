%%

%unicode 3.2
%public
%class UnicodePropList_Terminal_Punctuation_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
