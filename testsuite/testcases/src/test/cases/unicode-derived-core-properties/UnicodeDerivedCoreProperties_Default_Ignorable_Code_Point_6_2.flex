%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Default_Ignorable_Code_Point_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Default_Ignorable_Code_Point} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
