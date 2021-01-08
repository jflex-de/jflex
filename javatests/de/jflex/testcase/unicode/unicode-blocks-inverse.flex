package de.jflex.testcase.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;

%%

%unicode 5.1
%public
%class UnicodeBlocksInverseBlockScanner
%extends AbstractEnumeratedPropertyDefinedScanner

%type int

%init{
  super(/*maxCodepoint=*/0x10ffff);
%init}

%%

\P{Block:Latin Extended Additional} { setCurCharPropertyValue(yytext(), yylength(), "Not Latin Extended Additional"); }
\p{Block:Latin Extended Additional} { setCurCharPropertyValue(yytext(), yylength(), "Latin Extended Additional"); }

<<EOF>> { return YYEOF; }
