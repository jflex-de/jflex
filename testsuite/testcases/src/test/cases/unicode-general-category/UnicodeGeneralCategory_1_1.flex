%%

%unicode 1.1
%public
%class UnicodeGeneralCategory_1_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Cc} { setCurCharBlock("Cc"); }
\p{Cn} { setCurCharBlock("Cn"); }
\p{Ll} { setCurCharBlock("Ll"); }
\p{Lm} { setCurCharBlock("Lm"); }
\p{Lo} { setCurCharBlock("Lo"); }
\p{Lu} { setCurCharBlock("Lu"); }
\p{Mc} { setCurCharBlock("Mc"); }
\p{Mn} { setCurCharBlock("Mn"); }
\p{Nd} { setCurCharBlock("Nd"); }
\p{No} { setCurCharBlock("No"); }
\p{Pd} { setCurCharBlock("Pd"); }
\p{Pe} { setCurCharBlock("Pe"); }
\p{Po} { setCurCharBlock("Po"); }
\p{Ps} { setCurCharBlock("Ps"); }
\p{Sc} { setCurCharBlock("Sc"); }
\p{Sm} { setCurCharBlock("Sm"); }
\p{So} { setCurCharBlock("So"); }
\p{Zl} { setCurCharBlock("Zl"); }
\p{Zp} { setCurCharBlock("Zp"); }
\p{Zs} { setCurCharBlock("Zs"); }
