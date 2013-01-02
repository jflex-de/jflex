%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Changes_When_Lowercased_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Lowercased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
