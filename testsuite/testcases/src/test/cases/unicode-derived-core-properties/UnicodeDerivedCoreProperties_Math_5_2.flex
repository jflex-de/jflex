%%

%unicode 5.2
%public
%class UnicodeDerivedCoreProperties_Math_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
