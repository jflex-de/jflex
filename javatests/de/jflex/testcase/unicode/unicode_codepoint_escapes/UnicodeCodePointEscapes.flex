package de.jflex.testcase.unicode.unicode_codepoint_escapes;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%

%unicode
%public
%class UnicodeCodePointEscapes
%extends AbstractEnumeratedPropertyDefinedScanner

%type int

%init{
  super(0x10FFFF);
%init}

%%

<<EOF>> { return YYEOF; }
\u{1} { setCurCharPropertyValue(yytext(), "matched"); }
\u{000010} { setCurCharPropertyValue(yytext(), "matched"); }
\u{ CFF
    D00 } { setCurCharPropertyValue(yytext(), "matched"); }
\u{FFFF 10000 10001} { setCurCharPropertyValue(yytext(), "matched"); }

"\u{2}" { setCurCharPropertyValue(yytext(), "matched"); }
"\u{000011}" { setCurCharPropertyValue(yytext(), "matched"); }
"\u{ CFF D00 }" { setCurCharPropertyValue(yytext(), "matched"); }
"\u{FFF 1000 1001}" { setCurCharPropertyValue(yytext(), "matched"); }

[\u{3}\u{10FFFF}] { setCurCharPropertyValue(yytext(), "matched"); }

[^] { setCurCharPropertyValue(yytext(), "inverse matched"); }