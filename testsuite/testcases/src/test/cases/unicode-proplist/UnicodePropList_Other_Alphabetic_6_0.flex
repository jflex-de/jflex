%%

%unicode 6.0
%public
%class UnicodePropList_Other_Alphabetic_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
