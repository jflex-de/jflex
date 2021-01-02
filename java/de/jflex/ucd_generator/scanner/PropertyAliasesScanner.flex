/*
 * Copyright (C) 2009 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
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
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;

%%

%final
%class PropertyAliasesScanner
%extends AbstractPropertyAliasesScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state LONG_NAME, ADDITIONAL_ALIASES

%int
%function scan

%init{
  super(unicodeData);
%init}

Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  {Spaces} ("#" .*)? {NL} { clear(); }

  // scf       ; Simple_Case_Folding         ; sfc
  [^ \t\r\n;]+ { aliases.add(yytext()); }
  
  {ItemSeparator} { yybegin(LONG_NAME); }
}

<LONG_NAME> {
  [^ \t\r\n;]+ { longName = yytext(); }
  
  {ItemSeparator} { yybegin(ADDITIONAL_ALIASES); }

  <ADDITIONAL_ALIASES> {
    {Spaces} ("#" .*)? {NL} { addPropertyAliases(); yybegin(YYINITIAL); }
    <<EOF>> { addPropertyAliases(); return 0; }
  }
}

<ADDITIONAL_ALIASES> {
  [^ \t\r\n;#]+ { aliases.add(PropertyNameNormalizer.normalize(yytext())); }
  
  {ItemSeparator} { }
}
