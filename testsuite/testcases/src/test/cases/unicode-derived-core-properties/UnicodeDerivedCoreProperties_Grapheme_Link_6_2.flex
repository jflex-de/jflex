%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
