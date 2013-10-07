%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Case_Ignorable_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Case_Ignorable} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
