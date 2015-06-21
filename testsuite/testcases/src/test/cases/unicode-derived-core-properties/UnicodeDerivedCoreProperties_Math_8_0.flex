%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_Math_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
