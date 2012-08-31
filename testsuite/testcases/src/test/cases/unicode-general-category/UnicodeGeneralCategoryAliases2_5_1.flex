%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases2_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:Cc} { setCurCharPropertyValue("Cc"); }
\p{gc:Cf} { setCurCharPropertyValue("Cf"); }
\p{gc:Cn} { setCurCharPropertyValue("Cn"); }
\p{gc:Co} { setCurCharPropertyValue("Co"); }
\p{gc:Ll} { setCurCharPropertyValue("Ll"); }
\p{gc:Lm} { setCurCharPropertyValue("Lm"); }
\p{gc:Lo} { setCurCharPropertyValue("Lo"); }
\p{gc:Lt} { setCurCharPropertyValue("Lt"); }
\p{gc:Lu} { setCurCharPropertyValue("Lu"); }
\p{gc:Mc} { setCurCharPropertyValue("Mc"); }
\p{gc:Me} { setCurCharPropertyValue("Me"); }
\p{gc:Mn} { setCurCharPropertyValue("Mn"); }
\p{gc:Nd} { setCurCharPropertyValue("Nd"); }
\p{gc:Nl} { setCurCharPropertyValue("Nl"); }
\p{gc:No} { setCurCharPropertyValue("No"); }
\p{gc:Pc} { setCurCharPropertyValue("Pc"); }
\p{gc:Pd} { setCurCharPropertyValue("Pd"); }
\p{gc:Pe} { setCurCharPropertyValue("Pe"); }
\p{gc:Pf} { setCurCharPropertyValue("Pf"); }
\p{gc:Pi} { setCurCharPropertyValue("Pi"); }
\p{gc:Po} { setCurCharPropertyValue("Po"); }
\p{gc:Ps} { setCurCharPropertyValue("Ps"); }
\p{gc:Sc} { setCurCharPropertyValue("Sc"); }
\p{gc:Sk} { setCurCharPropertyValue("Sk"); }
\p{gc:Sm} { setCurCharPropertyValue("Sm"); }
\p{gc:So} { setCurCharPropertyValue("So"); }
\p{gc:Zl} { setCurCharPropertyValue("Zl"); }
\p{gc:Zp} { setCurCharPropertyValue("Zp"); }
\p{gc:Zs} { setCurCharPropertyValue("Zs"); }
