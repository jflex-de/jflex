%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Case_Ignorable_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Case_Ignorable} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
