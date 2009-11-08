%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
