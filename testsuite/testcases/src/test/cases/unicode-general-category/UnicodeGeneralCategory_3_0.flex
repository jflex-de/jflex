%%

%unicode 3.0
%public
%class UnicodeGeneralCategory_3_0

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Cc} { setCurCharBlock("Cc"); }
\p{Cf} { setCurCharBlock("Cf"); }
\p{Cn} { setCurCharBlock("Cn"); }
\p{Co} { setCurCharBlock("Co"); }
\p{Ll} { setCurCharBlock("Ll"); }
\p{Lm} { setCurCharBlock("Lm"); }
\p{Lo} { setCurCharBlock("Lo"); }
\p{Lt} { setCurCharBlock("Lt"); }
\p{Lu} { setCurCharBlock("Lu"); }
\p{Mc} { setCurCharBlock("Mc"); }
\p{Me} { setCurCharBlock("Me"); }
\p{Mn} { setCurCharBlock("Mn"); }
\p{Nd} { setCurCharBlock("Nd"); }
\p{Nl} { setCurCharBlock("Nl"); }
\p{No} { setCurCharBlock("No"); }
\p{Pc} { setCurCharBlock("Pc"); }
\p{Pd} { setCurCharBlock("Pd"); }
\p{Pe} { setCurCharBlock("Pe"); }
\p{Pf} { setCurCharBlock("Pf"); }
\p{Pi} { setCurCharBlock("Pi"); }
\p{Po} { setCurCharBlock("Po"); }
\p{Ps} { setCurCharBlock("Ps"); }
\p{Sc} { setCurCharBlock("Sc"); }
\p{Sk} { setCurCharBlock("Sk"); }
\p{Sm} { setCurCharBlock("Sm"); }
\p{So} { setCurCharBlock("So"); }
\p{Zl} { setCurCharBlock("Zl"); }
\p{Zp} { setCurCharBlock("Zp"); }
\p{Zs} { setCurCharBlock("Zs"); }
