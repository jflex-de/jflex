%%

%unicode 4.1
%public
%class UnicodePropList_Terminal_Punctuation_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
