%%

%unicode 8.0
%public
%class UnicodePropList_Terminal_Punctuation_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Terminal_Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
