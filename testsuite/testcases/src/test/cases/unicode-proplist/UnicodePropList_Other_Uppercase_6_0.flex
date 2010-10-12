%%

%unicode 6.0
%public
%class UnicodePropList_Other_Uppercase_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
