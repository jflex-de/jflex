%%

%unicode 5.1
%public
%class UnicodeNotGeneralCategory

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Ll} { setCurCharBlock("Ll"); }
\P{Ll} { setCurCharBlock("Not-Ll"); }
