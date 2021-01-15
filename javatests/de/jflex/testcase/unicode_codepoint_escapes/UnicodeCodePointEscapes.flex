/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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