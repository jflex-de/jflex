%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_Math_3_2

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
