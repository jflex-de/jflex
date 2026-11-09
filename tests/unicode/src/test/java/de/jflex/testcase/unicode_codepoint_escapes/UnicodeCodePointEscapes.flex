/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode_codepoint_escapes;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%

%unicode
%public
%class UnicodeCodePointEscapes
%extends AbstractEnumeratedPropertyDefinedScanner<String>

%type int

%init{
  // TODO(regisd) Introduced enum
  super(0x10FFFF, String.class);
%init}

%{
  private void setCurCharPropertyValue(String value) {
    setCurCharPropertyValue(yytext(), yylength(), value);
  }
%}

%%

<<EOF>> { return YYEOF; }
\u{1} { setCurCharPropertyValue("matched"); }
\u{000010} { setCurCharPropertyValue("matched"); }
\u{ CFF
    D00 } { setCurCharPropertyValue("matched"); }
\u{FFFF 10000 10001} { setCurCharPropertyValue("matched"); }

"\u{2}" { setCurCharPropertyValue("matched"); }
"\u{000011}" { setCurCharPropertyValue("matched"); }
"\u{ CFF D00 }" { setCurCharPropertyValue("matched"); }
"\u{FFF 1000 1001}" { setCurCharPropertyValue("matched"); }

[\u{3}\u{10FFFF}] { setCurCharPropertyValue("matched"); }

[^] { setCurCharPropertyValue("inverse matched"); }