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
import de.jflex.ucd_generator.ucd.UcdVersion;

/**
 * Scanner of the {@code UnicodeData.txt}.
 */
%%

%final
%class UnicodeDataScanner
%extends AbstractUnicodeDataScanner
%ctorarg UcdVersion ucdVersion
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state NAME_1, GENERAL_CATEGORY_2, CANONICAL_COMBINING_CLASS_3, BIDI_CLASS_4
%state DECOMPOSITION_TYPE_AND_MAPPING_5, NUMERIC_TYPE_6, NUMERIC_VALUE_INTEGER_7
%state NUMERIC_VALUE_OTHER_8, BIDI_MIRRORED_9, UNICODE_1_NAME_10, ISO_COMMENT_11
%state SIMPLE_UPPERCASE_MAPPING_12, SIMPLE_LOWERCASE_MAPPING_13
%state SIMPLE_TITLECASE_MAPPING_14

%state IGNORE_REST_OF_LINE

%int
%function scan

%init{
  super(ucdVersion, unicodeData);
%init}

Hex = [0-9A-Fa-f]+
Spaces = [ \t]*
NL = \n | \r | \r\n

%%

<YYINITIAL> { 
  {Spaces} ("#" .*)? {NL} { }
 
  // Code point field (Field #0)
  {Hex} { codePoint = Integer.parseInt(yytext(), 16); }
  
  ";" { yybegin(NAME_1); }

  <<EOF>> { handleFinalInterval(); return 0; }
}

<NAME_1> {
  "<" [^>,]+ "," {Spaces} "Last>" { isLastInRange = true; }
  
  [^;]+ { }
  
  ";" { yybegin(GENERAL_CATEGORY_2); }
}

<GENERAL_CATEGORY_2> {
  [^;]+ { genCatPropValue = yytext(); }
   
  ";" { yybegin(CANONICAL_COMBINING_CLASS_3); }
}

<IGNORE_REST_OF_LINE> {
  .* { reset(); yybegin(YYINITIAL); }
}

<CANONICAL_COMBINING_CLASS_3> {
  [^;]+ { }
  
  ";" { yybegin(BIDI_CLASS_4); }
}

<BIDI_CLASS_4> {
  [^;]+ { }
  
  ";" { yybegin(DECOMPOSITION_TYPE_AND_MAPPING_5); }
}

<DECOMPOSITION_TYPE_AND_MAPPING_5> {
  [^;]+ { }
  
  ";" { yybegin(NUMERIC_TYPE_6); }
}

<NUMERIC_TYPE_6> {
  [^;]+ { }
  
  ";" { yybegin(NUMERIC_VALUE_INTEGER_7); }
}

<NUMERIC_VALUE_INTEGER_7> {
  [^;]+ { }
  
  ";" { yybegin(NUMERIC_VALUE_OTHER_8); }
}

<NUMERIC_VALUE_OTHER_8> {
  [^;]+ { }
  
  ";" { yybegin(BIDI_MIRRORED_9); }
}

<BIDI_MIRRORED_9> {
  [^;]+ { }
  
  ";" { yybegin(UNICODE_1_NAME_10); }
}

<UNICODE_1_NAME_10> {
  [^;]+ { }
  
  ";" { yybegin(ISO_COMMENT_11); }
}

<ISO_COMMENT_11> {
  [^;]+ { }
  
  ";" { yybegin(SIMPLE_UPPERCASE_MAPPING_12); }
}

<SIMPLE_UPPERCASE_MAPPING_12> {
  [^;]+ { uppercaseMapping = yytext(); }
  
  ";" { yybegin(SIMPLE_LOWERCASE_MAPPING_13); }
}

<SIMPLE_LOWERCASE_MAPPING_13> {
  [^;]+ { lowercaseMapping = yytext(); }
  
  ";" { yybegin(SIMPLE_TITLECASE_MAPPING_14); }
}

<SIMPLE_TITLECASE_MAPPING_14> {
  [^\r\n]+ { titlecaseMapping = yytext(); }
  
  {NL} { handleEntry(); yybegin(YYINITIAL); }
  
  <<EOF>> { handleEntry(); handleFinalInterval(); return 0; }
}
