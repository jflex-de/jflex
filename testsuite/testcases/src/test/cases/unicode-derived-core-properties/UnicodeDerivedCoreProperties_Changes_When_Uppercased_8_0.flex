%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_Changes_When_Uppercased_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Changes_When_Uppercased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
