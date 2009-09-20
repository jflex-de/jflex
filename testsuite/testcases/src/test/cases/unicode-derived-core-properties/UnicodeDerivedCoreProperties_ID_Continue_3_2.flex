%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_ID_Continue_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ID_Continue} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
