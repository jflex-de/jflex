%%

%unicode 7.0
%public
%class UnicodeDerivedCoreProperties_Alphabetic_7_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
