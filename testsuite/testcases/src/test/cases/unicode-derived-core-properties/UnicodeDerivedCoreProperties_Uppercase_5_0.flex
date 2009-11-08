%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_Uppercase_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
