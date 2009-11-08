%%

%unicode 4.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
