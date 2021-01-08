package de.jflex.testcase.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;

%%

%unicode 10.0
%public
%class UnicodeInvalidBlockScanner
%extends AbstractEnumeratedPropertyDefinedScanner

%type int

%init{
  super(0xFFFFFF);
%init}

%%

<<EOF>>                                   { return YYEOF;}
\p{Block:This will never be a block name} { setCurCharPropertyValue(yytext(), yylength(), "Whatever"); }
[^]                                       { }
