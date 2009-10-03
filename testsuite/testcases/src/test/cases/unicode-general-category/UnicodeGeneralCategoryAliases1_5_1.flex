%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases1_5_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:Cc} { setCurCharPropertyValue("Cc"); }
\p{General_Category:Cf} { setCurCharPropertyValue("Cf"); }
\p{General_Category:Cn} { setCurCharPropertyValue("Cn"); }
\p{General_Category:Co} { setCurCharPropertyValue("Co"); }
\p{General_Category:Ll} { setCurCharPropertyValue("Ll"); }
\p{General_Category:Lm} { setCurCharPropertyValue("Lm"); }
\p{General_Category:Lo} { setCurCharPropertyValue("Lo"); }
\p{General_Category:Lt} { setCurCharPropertyValue("Lt"); }
\p{General_Category:Lu} { setCurCharPropertyValue("Lu"); }
\p{General_Category:Mc} { setCurCharPropertyValue("Mc"); }
\p{General_Category:Me} { setCurCharPropertyValue("Me"); }
\p{General_Category:Mn} { setCurCharPropertyValue("Mn"); }
\p{General_Category:Nd} { setCurCharPropertyValue("Nd"); }
\p{General_Category:Nl} { setCurCharPropertyValue("Nl"); }
\p{General_Category:No} { setCurCharPropertyValue("No"); }
\p{General_Category:Pc} { setCurCharPropertyValue("Pc"); }
\p{General_Category:Pd} { setCurCharPropertyValue("Pd"); }
\p{General_Category:Pe} { setCurCharPropertyValue("Pe"); }
\p{General_Category:Pf} { setCurCharPropertyValue("Pf"); }
\p{General_Category:Pi} { setCurCharPropertyValue("Pi"); }
\p{General_Category:Po} { setCurCharPropertyValue("Po"); }
\p{General_Category:Ps} { setCurCharPropertyValue("Ps"); }
\p{General_Category:Sc} { setCurCharPropertyValue("Sc"); }
\p{General_Category:Sk} { setCurCharPropertyValue("Sk"); }
\p{General_Category:Sm} { setCurCharPropertyValue("Sm"); }
\p{General_Category:So} { setCurCharPropertyValue("So"); }
\p{General_Category:Zl} { setCurCharPropertyValue("Zl"); }
\p{General_Category:Zp} { setCurCharPropertyValue("Zp"); }
\p{General_Category:Zs} { setCurCharPropertyValue("Zs"); }
