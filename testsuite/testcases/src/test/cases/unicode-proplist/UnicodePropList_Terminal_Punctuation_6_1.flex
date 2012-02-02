%%

%unicode 6.1
%public
%class UnicodePropList_Terminal_Punctuation_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
