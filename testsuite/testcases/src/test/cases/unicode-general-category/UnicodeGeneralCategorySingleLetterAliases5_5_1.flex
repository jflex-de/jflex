%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases5_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:Other} { setCurCharPropertyValue("C"); }
\p{General_Category:Letter} { setCurCharPropertyValue("L"); }
\p{General_Category:Mark} { setCurCharPropertyValue("M"); }
\p{General_Category:Number} { setCurCharPropertyValue("N"); }
\p{General_Category:Punctuation} { setCurCharPropertyValue("P"); }
\p{General_Category:Symbol} { setCurCharPropertyValue("S"); }
\p{General_Category:Separator} { setCurCharPropertyValue("Z"); }
