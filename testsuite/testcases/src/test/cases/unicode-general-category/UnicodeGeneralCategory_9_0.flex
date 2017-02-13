%%

%unicode 9.0
%public
%class UnicodeGeneralCategory_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Cc} { setCurCharPropertyValue("Cc"); }
\p{Cf} { setCurCharPropertyValue("Cf"); }
\p{Cn} { setCurCharPropertyValue("Cn"); }
\p{Co} { setCurCharPropertyValue("Co"); }
\p{Ll} { setCurCharPropertyValue("Ll"); }
\p{Lm} { setCurCharPropertyValue("Lm"); }
\p{Lo} { setCurCharPropertyValue("Lo"); }
\p{Lt} { setCurCharPropertyValue("Lt"); }
\p{Lu} { setCurCharPropertyValue("Lu"); }
\p{Mc} { setCurCharPropertyValue("Mc"); }
\p{Me} { setCurCharPropertyValue("Me"); }
\p{Mn} { setCurCharPropertyValue("Mn"); }
\p{Nd} { setCurCharPropertyValue("Nd"); }
\p{Nl} { setCurCharPropertyValue("Nl"); }
\p{No} { setCurCharPropertyValue("No"); }
\p{Pc} { setCurCharPropertyValue("Pc"); }
\p{Pd} { setCurCharPropertyValue("Pd"); }
\p{Pe} { setCurCharPropertyValue("Pe"); }
\p{Pf} { setCurCharPropertyValue("Pf"); }
\p{Pi} { setCurCharPropertyValue("Pi"); }
\p{Po} { setCurCharPropertyValue("Po"); }
\p{Ps} { setCurCharPropertyValue("Ps"); }
\p{Sc} { setCurCharPropertyValue("Sc"); }
\p{Sk} { setCurCharPropertyValue("Sk"); }
\p{Sm} { setCurCharPropertyValue("Sm"); }
\p{So} { setCurCharPropertyValue("So"); }
\p{Zl} { setCurCharPropertyValue("Zl"); }
\p{Zp} { setCurCharPropertyValue("Zp"); }
\p{Zs} { setCurCharPropertyValue("Zs"); }
