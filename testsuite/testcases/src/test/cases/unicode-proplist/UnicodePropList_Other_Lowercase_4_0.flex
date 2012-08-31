%%

%unicode 4.0
%public
%class UnicodePropList_Other_Lowercase_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
