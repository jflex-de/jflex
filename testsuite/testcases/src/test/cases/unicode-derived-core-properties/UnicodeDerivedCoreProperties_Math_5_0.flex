%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_Math_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
