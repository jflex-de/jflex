%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Changes_When_Casemapped_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Casemapped} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
