%%

%unicode 6.1
%public
%class UnicodePropList_Soft_Dotted_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
