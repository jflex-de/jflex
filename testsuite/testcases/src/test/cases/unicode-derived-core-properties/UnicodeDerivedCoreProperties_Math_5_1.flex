%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_Math_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
