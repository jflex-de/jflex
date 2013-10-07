%%

%unicode 6.3
%public
%class UnicodePropList_Terminal_Punctuation_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
