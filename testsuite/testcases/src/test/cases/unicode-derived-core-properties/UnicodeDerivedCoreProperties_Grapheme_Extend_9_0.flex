%%

%unicode 9.0
%public
%class UnicodeDerivedCoreProperties_Grapheme_Extend_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Grapheme_Extend} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
