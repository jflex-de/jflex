%%

%unicode 5.2
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
