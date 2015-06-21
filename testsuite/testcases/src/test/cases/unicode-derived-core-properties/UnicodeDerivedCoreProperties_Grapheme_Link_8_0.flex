%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
