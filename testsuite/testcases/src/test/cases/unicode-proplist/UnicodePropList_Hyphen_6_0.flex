%%

%unicode 6.0
%public
%class UnicodePropList_Hyphen_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
