%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_Changes_When_Titlecased_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Changes_When_Titlecased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
