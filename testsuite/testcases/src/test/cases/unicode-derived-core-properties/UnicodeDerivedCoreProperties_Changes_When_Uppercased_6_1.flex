%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Changes_When_Uppercased_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Uppercased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
