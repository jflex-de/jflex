%%

%unicode 3.1
%public
%class UnicodeDerivedCoreProperties_Math_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
