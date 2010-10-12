%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Changes_When_Titlecased_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Changes_When_Titlecased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
