%%

%unicode 5.0
%public
%class UnicodePropList_Radical_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
