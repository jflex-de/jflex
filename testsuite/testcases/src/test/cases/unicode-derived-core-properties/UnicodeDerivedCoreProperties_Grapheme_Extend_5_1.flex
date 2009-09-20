%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
