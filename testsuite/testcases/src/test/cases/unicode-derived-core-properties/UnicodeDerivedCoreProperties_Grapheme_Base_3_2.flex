%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_Grapheme_Base_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Base} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
