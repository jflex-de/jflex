%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
