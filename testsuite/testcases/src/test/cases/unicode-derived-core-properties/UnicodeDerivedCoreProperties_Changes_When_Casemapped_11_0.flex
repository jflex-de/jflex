%%

%unicode 11.0
%public
%class UnicodeDerivedCoreProperties_Changes_When_Casemapped_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Changes_When_Casemapped} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
