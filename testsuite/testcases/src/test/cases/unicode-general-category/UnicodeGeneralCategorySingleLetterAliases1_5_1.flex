%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases1_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:C} { setCurCharPropertyValue("C"); }
\p{General_Category:L} { setCurCharPropertyValue("L"); }
\p{General_Category:M} { setCurCharPropertyValue("M"); }
\p{General_Category:N} { setCurCharPropertyValue("N"); }
\p{General_Category:P} { setCurCharPropertyValue("P"); }
\p{General_Category:S} { setCurCharPropertyValue("S"); }
\p{General_Category:Z} { setCurCharPropertyValue("Z"); }
