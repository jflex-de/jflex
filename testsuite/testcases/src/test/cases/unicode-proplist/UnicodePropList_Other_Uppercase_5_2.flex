%%

%unicode 5.2
%public
%class UnicodePropList_Other_Uppercase_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
