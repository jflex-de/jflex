%%

%unicode 9.0
%public
%class UnicodeDerivedCoreProperties_Changes_When_Titlecased_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Changes_When_Titlecased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
