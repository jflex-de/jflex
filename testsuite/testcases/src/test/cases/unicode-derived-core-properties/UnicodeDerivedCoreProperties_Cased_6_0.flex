%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Cased_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Cased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
