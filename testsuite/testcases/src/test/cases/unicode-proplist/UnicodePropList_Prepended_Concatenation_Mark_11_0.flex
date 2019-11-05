%%

%unicode 11.0
%public
%class UnicodePropList_Prepended_Concatenation_Mark_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Prepended_Concatenation_Mark} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
