%%

%unicode 2.1
%public
%class UnicodePropList_Terminal_Punctuation_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Terminal Punctuation} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
