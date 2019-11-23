%%

%unicode 12.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
