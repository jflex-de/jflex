%%

%unicode 4.0
%public
%class UnicodeDerivedCoreProperties_Alphabetic_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
