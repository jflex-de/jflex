%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
