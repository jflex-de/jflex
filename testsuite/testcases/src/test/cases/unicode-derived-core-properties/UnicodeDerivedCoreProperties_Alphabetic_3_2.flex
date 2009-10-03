%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_Alphabetic_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
