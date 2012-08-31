%%

%unicode 4.0
%public
%class UnicodeDerivedCoreProperties_Alphabetic_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
