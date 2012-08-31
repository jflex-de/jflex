%%

%unicode 5.0
%public
%class UnicodeGeneralCategorySingleLetter_5_0

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{C} { setCurCharPropertyValue("C"); }
\p{L} { setCurCharPropertyValue("L"); }
\p{M} { setCurCharPropertyValue("M"); }
\p{N} { setCurCharPropertyValue("N"); }
\p{P} { setCurCharPropertyValue("P"); }
\p{S} { setCurCharPropertyValue("S"); }
\p{Z} { setCurCharPropertyValue("Z"); }
