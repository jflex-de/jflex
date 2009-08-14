%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases2_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:Cc} { setCurCharBlock("Cc"); }
\p{gc:Cf} { setCurCharBlock("Cf"); }
\p{gc:Cn} { setCurCharBlock("Cn"); }
\p{gc:Co} { setCurCharBlock("Co"); }
\p{gc:Ll} { setCurCharBlock("Ll"); }
\p{gc:Lm} { setCurCharBlock("Lm"); }
\p{gc:Lo} { setCurCharBlock("Lo"); }
\p{gc:Lt} { setCurCharBlock("Lt"); }
\p{gc:Lu} { setCurCharBlock("Lu"); }
\p{gc:Mc} { setCurCharBlock("Mc"); }
\p{gc:Me} { setCurCharBlock("Me"); }
\p{gc:Mn} { setCurCharBlock("Mn"); }
\p{gc:Nd} { setCurCharBlock("Nd"); }
\p{gc:Nl} { setCurCharBlock("Nl"); }
\p{gc:No} { setCurCharBlock("No"); }
\p{gc:Pc} { setCurCharBlock("Pc"); }
\p{gc:Pd} { setCurCharBlock("Pd"); }
\p{gc:Pe} { setCurCharBlock("Pe"); }
\p{gc:Pf} { setCurCharBlock("Pf"); }
\p{gc:Pi} { setCurCharBlock("Pi"); }
\p{gc:Po} { setCurCharBlock("Po"); }
\p{gc:Ps} { setCurCharBlock("Ps"); }
\p{gc:Sc} { setCurCharBlock("Sc"); }
\p{gc:Sk} { setCurCharBlock("Sk"); }
\p{gc:Sm} { setCurCharBlock("Sm"); }
\p{gc:So} { setCurCharBlock("So"); }
\p{gc:Zl} { setCurCharBlock("Zl"); }
\p{gc:Zp} { setCurCharBlock("Zp"); }
\p{gc:Zs} { setCurCharBlock("Zs"); }
