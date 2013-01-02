%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Case_Ignorable_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Case_Ignorable} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
