package jflex.core.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%

%unicode 2.0
%public
%class UnicodeAge_2_0_age_2_0
%extends AbstractEnumeratedPropertyDefinedScanner

%type int
%standalone

%%

<<EOF>>     { return YYEOF;}
\p{Age:2.0} { setCurCharPropertyValue(yytext(), "Age:2.0"); }
[^] { }
