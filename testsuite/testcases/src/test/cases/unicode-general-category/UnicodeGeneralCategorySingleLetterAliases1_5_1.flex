%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases1_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:C} { setCurCharBlock("C"); }
\p{General_Category:L} { setCurCharBlock("L"); }
\p{General_Category:M} { setCurCharBlock("M"); }
\p{General_Category:N} { setCurCharBlock("N"); }
\p{General_Category:P} { setCurCharBlock("P"); }
\p{General_Category:S} { setCurCharBlock("S"); }
\p{General_Category:Z} { setCurCharBlock("Z"); }
