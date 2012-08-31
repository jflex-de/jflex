%%

%unicode 6.1
%public
%class UnicodePropList_Radical_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
