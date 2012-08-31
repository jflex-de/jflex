%%

%unicode 5.2
%public
%class UnicodePropList_Radical_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
