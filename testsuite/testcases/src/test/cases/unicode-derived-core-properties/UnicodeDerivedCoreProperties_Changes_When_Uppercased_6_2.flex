%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Changes_When_Uppercased_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Uppercased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
