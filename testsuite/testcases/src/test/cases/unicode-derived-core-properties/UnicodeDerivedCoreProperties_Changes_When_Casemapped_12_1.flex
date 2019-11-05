%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Changes_When_Casemapped_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Changes_When_Casemapped} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
