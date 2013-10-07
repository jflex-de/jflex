%%

%unicode 6.3
%public
%class UnicodePropList_Radical_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
