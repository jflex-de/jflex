%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Changes_When_Casefolded_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Casefolded} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
