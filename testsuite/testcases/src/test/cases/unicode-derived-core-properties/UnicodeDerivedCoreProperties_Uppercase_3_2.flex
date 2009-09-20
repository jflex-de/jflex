%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_Uppercase_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
