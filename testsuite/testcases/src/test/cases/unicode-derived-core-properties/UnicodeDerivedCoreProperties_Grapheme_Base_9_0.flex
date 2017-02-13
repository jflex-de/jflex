%%

%unicode 9.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Base_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Base} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
