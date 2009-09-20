%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_Grapheme_Base_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Grapheme_Base} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
