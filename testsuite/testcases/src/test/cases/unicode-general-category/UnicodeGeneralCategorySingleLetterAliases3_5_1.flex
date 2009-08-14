%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases3_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Other} { setCurCharBlock("C"); }
\p{Letter} { setCurCharBlock("L"); }
\p{Mark} { setCurCharBlock("M"); }
\p{Number} { setCurCharBlock("N"); }
\p{Punctuation} { setCurCharBlock("P"); }
\p{Symbol} { setCurCharBlock("S"); }
\p{Separator} { setCurCharBlock("Z"); }
