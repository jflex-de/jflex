%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Alphabetic_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
