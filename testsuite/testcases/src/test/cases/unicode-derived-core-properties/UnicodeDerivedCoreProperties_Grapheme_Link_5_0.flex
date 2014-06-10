%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_5_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
