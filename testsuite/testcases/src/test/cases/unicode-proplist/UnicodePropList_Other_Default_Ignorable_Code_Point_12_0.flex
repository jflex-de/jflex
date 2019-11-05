%%

%unicode 12.0
%public
%class UnicodePropList_Other_Default_Ignorable_Code_Point_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Other_Default_Ignorable_Code_Point} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
