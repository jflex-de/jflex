%%

%unicode 3.0
%public
%class UnicodePropList_Terminal_Punctuation_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
