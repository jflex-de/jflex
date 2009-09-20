%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
