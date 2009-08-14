%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases1_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:Cc} { setCurCharBlock("Cc"); }
\p{General_Category:Cf} { setCurCharBlock("Cf"); }
\p{General_Category:Cn} { setCurCharBlock("Cn"); }
\p{General_Category:Co} { setCurCharBlock("Co"); }
\p{General_Category:Ll} { setCurCharBlock("Ll"); }
\p{General_Category:Lm} { setCurCharBlock("Lm"); }
\p{General_Category:Lo} { setCurCharBlock("Lo"); }
\p{General_Category:Lt} { setCurCharBlock("Lt"); }
\p{General_Category:Lu} { setCurCharBlock("Lu"); }
\p{General_Category:Mc} { setCurCharBlock("Mc"); }
\p{General_Category:Me} { setCurCharBlock("Me"); }
\p{General_Category:Mn} { setCurCharBlock("Mn"); }
\p{General_Category:Nd} { setCurCharBlock("Nd"); }
\p{General_Category:Nl} { setCurCharBlock("Nl"); }
\p{General_Category:No} { setCurCharBlock("No"); }
\p{General_Category:Pc} { setCurCharBlock("Pc"); }
\p{General_Category:Pd} { setCurCharBlock("Pd"); }
\p{General_Category:Pe} { setCurCharBlock("Pe"); }
\p{General_Category:Pf} { setCurCharBlock("Pf"); }
\p{General_Category:Pi} { setCurCharBlock("Pi"); }
\p{General_Category:Po} { setCurCharBlock("Po"); }
\p{General_Category:Ps} { setCurCharBlock("Ps"); }
\p{General_Category:Sc} { setCurCharBlock("Sc"); }
\p{General_Category:Sk} { setCurCharBlock("Sk"); }
\p{General_Category:Sm} { setCurCharBlock("Sm"); }
\p{General_Category:So} { setCurCharBlock("So"); }
\p{General_Category:Zl} { setCurCharBlock("Zl"); }
\p{General_Category:Zp} { setCurCharBlock("Zp"); }
\p{General_Category:Zs} { setCurCharBlock("Zs"); }
