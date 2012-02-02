%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Cased_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Cased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
