%%

%unicode 3.2
%public
%class UnicodePropList_Soft_Dotted_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
