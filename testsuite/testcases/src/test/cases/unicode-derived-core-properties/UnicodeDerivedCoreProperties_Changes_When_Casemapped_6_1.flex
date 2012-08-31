%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Changes_When_Casemapped_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Casemapped} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
