/*
 * Copyright (C) 2013 Steve Rowe <sarowe@gmail.com>
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
package de.jflex.testcase.unicode.unicode_6_3;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
%%
// This flex spec was generated by UnicodeAgeGenerator
// from java/de/jflex/migration/unicodedatatest/UnicodeAgeSubtraction.flex.vm

%unicode 6.3
%public
%class UnicodeAge_6_3_age_subtraction
%extends AbstractEnumeratedPropertyDefinedScanner

%type int


%init{
  super(1114111);
%init}

%%

<<EOF>>                    { return YYEOF; }
[\p{Age:2.0}--\p{Age:1.1}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:2.0}--\\p{Age:1.1}]");
}
[\p{Age:2.1}--\p{Age:2.0}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:2.1}--\\p{Age:2.0}]");
}
[\p{Age:3.0}--\p{Age:2.1}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:3.0}--\\p{Age:2.1}]");
}
[\p{Age:3.1}--\p{Age:3.0}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:3.1}--\\p{Age:3.0}]");
}
[\p{Age:3.2}--\p{Age:3.1}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:3.2}--\\p{Age:3.1}]");
}
[\p{Age:4.0}--\p{Age:3.2}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:4.0}--\\p{Age:3.2}]");
}
[\p{Age:4.1}--\p{Age:4.0}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:4.1}--\\p{Age:4.0}]");
}
[\p{Age:5.0}--\p{Age:4.1}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:5.0}--\\p{Age:4.1}]");
}
[\p{Age:5.1}--\p{Age:5.0}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:5.1}--\\p{Age:5.0}]");
}
[\p{Age:5.2}--\p{Age:5.1}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:5.2}--\\p{Age:5.1}]");
}
[\p{Age:6.0}--\p{Age:5.2}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:6.0}--\\p{Age:5.2}]");
}
[\p{Age:6.1}--\p{Age:6.0}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:6.1}--\\p{Age:6.0}]");
}
[\p{Age:6.2}--\p{Age:6.1}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:6.2}--\\p{Age:6.1}]");
}
[\p{Age:6.3}--\p{Age:6.2}] {
  setCurCharPropertyValue(yytext(), "[\\p{Age:6.3}--\\p{Age:6.2}]");
}
[^] { }
