%%

%unicode 3.2
%public
%class UnicodePropList_Radical_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
