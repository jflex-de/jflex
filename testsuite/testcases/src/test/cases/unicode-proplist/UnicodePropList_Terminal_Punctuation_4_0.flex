%%

%unicode 4.0
%public
%class UnicodePropList_Terminal_Punctuation_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
