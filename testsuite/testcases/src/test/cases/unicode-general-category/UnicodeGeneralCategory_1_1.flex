%%

%unicode 1.1
%public
%class UnicodeGeneralCategory_1_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Cc} { setCurCharPropertyValue("Cc"); }
\p{Cn} { setCurCharPropertyValue("Cn"); }
\p{Ll} { setCurCharPropertyValue("Ll"); }
\p{Lm} { setCurCharPropertyValue("Lm"); }
\p{Lo} { setCurCharPropertyValue("Lo"); }
\p{Lu} { setCurCharPropertyValue("Lu"); }
\p{Mc} { setCurCharPropertyValue("Mc"); }
\p{Mn} { setCurCharPropertyValue("Mn"); }
\p{Nd} { setCurCharPropertyValue("Nd"); }
\p{No} { setCurCharPropertyValue("No"); }
\p{Pd} { setCurCharPropertyValue("Pd"); }
\p{Pe} { setCurCharPropertyValue("Pe"); }
\p{Po} { setCurCharPropertyValue("Po"); }
\p{Ps} { setCurCharPropertyValue("Ps"); }
\p{Sc} { setCurCharPropertyValue("Sc"); }
\p{Sm} { setCurCharPropertyValue("Sm"); }
\p{So} { setCurCharPropertyValue("So"); }
\p{Zl} { setCurCharPropertyValue("Zl"); }
\p{Zp} { setCurCharPropertyValue("Zp"); }
\p{Zs} { setCurCharPropertyValue("Zs"); }
