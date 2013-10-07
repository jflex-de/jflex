%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Alphabetic_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
