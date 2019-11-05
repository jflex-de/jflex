%%

%unicode 10.0
%public
%class UnicodeDerivedCoreProperties_Alphabetic_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
