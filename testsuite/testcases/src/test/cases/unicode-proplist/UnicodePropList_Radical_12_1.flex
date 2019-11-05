%%

%unicode 12.1
%public
%class UnicodePropList_Radical_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
