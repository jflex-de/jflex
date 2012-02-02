%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_ID_Continue_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
