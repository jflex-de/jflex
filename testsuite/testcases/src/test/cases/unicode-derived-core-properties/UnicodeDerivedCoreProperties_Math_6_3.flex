%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Math_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
