%%

%unicode 3.1
%public
%class UnicodeGeneralCategorySingleLetter_3_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{C} { setCurCharBlock("C"); }
\p{L} { setCurCharBlock("L"); }
\p{M} { setCurCharBlock("M"); }
\p{N} { setCurCharBlock("N"); }
\p{P} { setCurCharBlock("P"); }
\p{S} { setCurCharBlock("S"); }
\p{Z} { setCurCharBlock("Z"); }
