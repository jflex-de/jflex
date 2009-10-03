%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases3_5_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Other} { setCurCharPropertyValue("C"); }
\p{Letter} { setCurCharPropertyValue("L"); }
\p{Mark} { setCurCharPropertyValue("M"); }
\p{Number} { setCurCharPropertyValue("N"); }
\p{Punctuation} { setCurCharPropertyValue("P"); }
\p{Symbol} { setCurCharPropertyValue("S"); }
\p{Separator} { setCurCharPropertyValue("Z"); }
