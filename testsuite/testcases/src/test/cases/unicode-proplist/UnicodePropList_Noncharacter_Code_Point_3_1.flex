%%

%unicode 3.1
%public
%class UnicodePropList_Noncharacter_Code_Point_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Noncharacter_Code_Point} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
