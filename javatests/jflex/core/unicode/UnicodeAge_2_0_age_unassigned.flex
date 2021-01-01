package jflex.core.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%

%unicode 2.0
%public
%class UnicodeAge_2_0_age_unassigned
%extends AbstractEnumeratedPropertyDefinedScanner

%type int
%standalone

%%

<<EOF>>            { return YYEOF; }
\p{Age:Unassigned} { setCurCharPropertyValue(yytext(), "Age:Unassigned"); }
[^] { }
