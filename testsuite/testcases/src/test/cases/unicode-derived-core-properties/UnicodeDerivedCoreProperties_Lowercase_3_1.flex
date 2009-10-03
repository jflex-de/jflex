%%

%unicode 3.1
%public
%class UnicodeDerivedCoreProperties_Lowercase_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
