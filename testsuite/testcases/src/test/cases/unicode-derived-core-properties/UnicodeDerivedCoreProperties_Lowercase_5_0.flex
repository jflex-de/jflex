%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_Lowercase_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
