%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Case_Ignorable_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Case_Ignorable} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
