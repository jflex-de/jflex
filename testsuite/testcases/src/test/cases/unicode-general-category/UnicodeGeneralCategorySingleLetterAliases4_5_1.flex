%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases4_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:Other} { setCurCharBlock("C"); }
\p{gc:Letter} { setCurCharBlock("L"); }
\p{gc:Mark} { setCurCharBlock("M"); }
\p{gc:Number} { setCurCharBlock("N"); }
\p{gc:Punctuation} { setCurCharBlock("P"); }
\p{gc:Symbol} { setCurCharBlock("S"); }
\p{gc:Separator} { setCurCharBlock("Z"); }
