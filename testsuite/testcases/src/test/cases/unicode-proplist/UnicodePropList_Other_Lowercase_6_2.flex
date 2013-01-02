%%

%unicode 6.2
%public
%class UnicodePropList_Other_Lowercase_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
