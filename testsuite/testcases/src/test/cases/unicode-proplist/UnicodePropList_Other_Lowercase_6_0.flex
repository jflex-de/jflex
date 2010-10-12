%%

%unicode 6.0
%public
%class UnicodePropList_Other_Lowercase_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
