%%

%unicode 6.1
%public
%class UnicodePropList_Other_Uppercase_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
