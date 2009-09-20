%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_Lowercase_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
