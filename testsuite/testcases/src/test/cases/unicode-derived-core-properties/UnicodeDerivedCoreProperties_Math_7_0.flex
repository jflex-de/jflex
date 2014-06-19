%%

%unicode 7.0
%public
%class UnicodeDerivedCoreProperties_Math_7_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
