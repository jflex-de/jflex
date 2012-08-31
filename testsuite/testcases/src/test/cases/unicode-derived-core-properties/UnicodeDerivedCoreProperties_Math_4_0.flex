%%

%unicode 4.0
%public
%class UnicodeDerivedCoreProperties_Math_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
