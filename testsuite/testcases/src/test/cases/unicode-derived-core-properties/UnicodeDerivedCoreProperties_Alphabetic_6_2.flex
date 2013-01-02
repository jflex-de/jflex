%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Alphabetic_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
