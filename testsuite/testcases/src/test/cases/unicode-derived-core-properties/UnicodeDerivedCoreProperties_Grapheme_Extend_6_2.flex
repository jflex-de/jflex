%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
