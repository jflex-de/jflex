%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Alphabetic_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
