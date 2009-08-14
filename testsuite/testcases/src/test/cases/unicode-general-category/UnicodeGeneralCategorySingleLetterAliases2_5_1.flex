%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases2_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:C} { setCurCharBlock("C"); }
\p{gc:L} { setCurCharBlock("L"); }
\p{gc:M} { setCurCharBlock("M"); }
\p{gc:N} { setCurCharBlock("N"); }
\p{gc:P} { setCurCharBlock("P"); }
\p{gc:S} { setCurCharBlock("S"); }
\p{gc:Z} { setCurCharBlock("Z"); }
