%%

%unicode 5.2
%public
%class UnicodeDerivedCoreProperties_Changes_When_Casemapped_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Changes_When_Casemapped} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
