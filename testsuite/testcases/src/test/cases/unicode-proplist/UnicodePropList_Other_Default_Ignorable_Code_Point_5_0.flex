%%

%unicode 5.0
%public
%class UnicodePropList_Other_Default_Ignorable_Code_Point_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Default_Ignorable_Code_Point} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
