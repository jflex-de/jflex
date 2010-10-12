%%

%unicode 6.0
%public
%class UnicodePropList_Terminal_Punctuation_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
