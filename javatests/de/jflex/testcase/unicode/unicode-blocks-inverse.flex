package de.jflex.testcase.unicode;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;

%%

%unicode 5.1
%public
%class UnicodeBlocksInverseBlockScanner
%extends AbstractEnumeratedPropertyDefinedScanner<TestingBlock>

%type int

%init{
  super(/*maxCodepoint=*/0x10ffff, TestingBlock.class);
%init}

%%

\P{Block:Latin Extended Additional} { setCurCharPropertyValue(yytext(), yylength(), TestingBlock.NOT_LATIN_EXTENDED_ADDITIONAL); }
\p{Block:Latin Extended Additional} { setCurCharPropertyValue(yytext(), yylength(), TestingBlock.LATIN_EXTENDED_ADDITIONAL); }

<<EOF>> { return YYEOF; }
