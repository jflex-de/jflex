%%

%unicode 4.1
%public
%class UnicodePropList_Terminal_Punctuation_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
