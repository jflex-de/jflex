%%

%unicode 11.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Base_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Base} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
