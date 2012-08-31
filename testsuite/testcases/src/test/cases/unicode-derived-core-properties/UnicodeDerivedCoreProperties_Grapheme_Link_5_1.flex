%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Link_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Link} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
