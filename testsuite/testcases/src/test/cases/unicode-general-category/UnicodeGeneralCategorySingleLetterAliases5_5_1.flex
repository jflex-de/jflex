%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases5_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:Other} { setCurCharBlock("C"); }
\p{General_Category:Letter} { setCurCharBlock("L"); }
\p{General_Category:Mark} { setCurCharBlock("M"); }
\p{General_Category:Number} { setCurCharBlock("N"); }
\p{General_Category:Punctuation} { setCurCharBlock("P"); }
\p{General_Category:Symbol} { setCurCharBlock("S"); }
\p{General_Category:Separator} { setCurCharBlock("Z"); }
