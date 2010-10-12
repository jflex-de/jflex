%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Math_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
