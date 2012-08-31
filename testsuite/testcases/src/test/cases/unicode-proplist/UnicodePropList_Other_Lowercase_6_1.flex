%%

%unicode 6.1
%public
%class UnicodePropList_Other_Lowercase_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
