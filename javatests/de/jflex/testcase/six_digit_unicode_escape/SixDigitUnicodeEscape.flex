package de.jflex.testcase.six_digit_unicode_escape;

import de.jflex.testcase.resources.AbstractUnicodeAllEnumeratedProperty;
%%

%unicode
%public
%class SixDigitUnicodeEscape
%extends AbstractUnicodeAllEnumeratedProperty

%type int
%standalone
%buffer 300 

%{
  private void setCurCharPropertyValue(String propertyValue) {
    setCurCharPropertyValue(propertyValue, yylength(), yytext());
  }
%}

%%

<<EOF>> { eof(); return 1; }
\U000001 { setCurCharPropertyValue("matched"); }
[\U000003] { setCurCharPropertyValue("matched"); }
"\U000004" { setCurCharPropertyValue("matched"); }
[\U000005-\U10FFFD] { setCurCharPropertyValue("matched"); }
[^\U000001\U000003-\U10FFFD] { setCurCharPropertyValue("inverse matched"); }
