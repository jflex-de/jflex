package jflex.core.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%

%unicode 2.0
%public
%class UnicodeAge_2_0_age_subtraction
%extends AbstractEnumeratedPropertyDefinedScanner

%type int
%standalone

%%

<<EOF>>                    { return YYEOF; }
[\p{Age:2.0}--\p{Age:1.1}] { setCurCharPropertyValue(yytext(), "[\\p{Age:2.0}--\\p{Age:1.1}]"); }
[^] { }
