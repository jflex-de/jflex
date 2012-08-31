%%

%unicode 6.0
%public
%class UnicodePropList_Radical_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
