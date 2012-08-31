%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Base_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Base} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
