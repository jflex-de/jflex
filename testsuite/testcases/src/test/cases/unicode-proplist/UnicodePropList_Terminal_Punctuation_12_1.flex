%%

%unicode 12.1
%public
%class UnicodePropList_Terminal_Punctuation_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
