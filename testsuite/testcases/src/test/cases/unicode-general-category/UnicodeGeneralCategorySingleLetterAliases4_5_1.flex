%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases4_5_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:Other} { setCurCharPropertyValue("C"); }
\p{gc:Letter} { setCurCharPropertyValue("L"); }
\p{gc:Mark} { setCurCharPropertyValue("M"); }
\p{gc:Number} { setCurCharPropertyValue("N"); }
\p{gc:Punctuation} { setCurCharPropertyValue("P"); }
\p{gc:Symbol} { setCurCharPropertyValue("S"); }
\p{gc:Separator} { setCurCharPropertyValue("Z"); }
