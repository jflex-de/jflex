%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
