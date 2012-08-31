%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
