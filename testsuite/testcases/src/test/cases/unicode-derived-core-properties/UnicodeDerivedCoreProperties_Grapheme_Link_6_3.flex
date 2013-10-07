%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
