%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Changes_When_Lowercased_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Changes_When_Lowercased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
