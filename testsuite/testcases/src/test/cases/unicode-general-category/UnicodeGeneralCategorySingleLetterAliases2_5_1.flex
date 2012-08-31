%%

%unicode 5.1
%public
%class UnicodeGeneralCategorySingleLetterAliases2_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:C} { setCurCharPropertyValue("C"); }
\p{gc:L} { setCurCharPropertyValue("L"); }
\p{gc:M} { setCurCharPropertyValue("M"); }
\p{gc:N} { setCurCharPropertyValue("N"); }
\p{gc:P} { setCurCharPropertyValue("P"); }
\p{gc:S} { setCurCharPropertyValue("S"); }
\p{gc:Z} { setCurCharPropertyValue("Z"); }
