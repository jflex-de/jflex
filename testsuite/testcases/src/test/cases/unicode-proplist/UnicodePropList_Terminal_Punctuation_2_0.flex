%%

%unicode 2.0
%public
%class UnicodePropList_Terminal_Punctuation_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Terminal Punctuation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
