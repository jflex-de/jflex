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
import de.jflex.ucd_generator.ucd.NamedCodepointRange;

%%

%class EnumeratedPropertyFileScanner
%extends AbstractEnumeratedPropertyFileScanner
%ctorarg UnicodeData unicodeData
%ctorarg String defaultPropertyName
%ctorarg String defaultPropertyValue

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_NAME, PROPERTY_VALUE
%state COMMENT_LINE, DEFAULT_PROPERTY_VALUE

%int
%function scan

%init{
  super(unicodeData, defaultPropertyName, defaultPropertyValue);
%init}

Hex = [0-9A-Fa-f]{4,6}
Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  /* # Property:	Grapheme_Cluster_Break */
  {Spaces} "#" {Spaces} "Property:" {Spaces} { yybegin(PROPERTY_NAME); }

  /* # @missing: 0000..10FFFF; Other */
  {Spaces} "#" {Spaces} "@missing:" {Spaces} { yybegin(DEFAULT_PROPERTY_VALUE); }

  {Spaces} "#" { yybegin(COMMENT_LINE); }
  
  {Spaces} {NL} { }
  
  {Hex} { start = Integer.parseInt(yytext(), 16); yybegin(BEGIN_RANGE); }
}

<COMMENT_LINE> {
  .* {NL}? { yybegin(YYINITIAL); }
}

<BEGIN_RANGE> {
  ".." { yybegin(END_RANGE); }
  
  {ItemSeparator} { end = start; yybegin(PROPERTY_VALUE); }
}

<END_RANGE> {
  {Hex} { end = Integer.parseInt(yytext(), 16); }
  
  {ItemSeparator} { yybegin(PROPERTY_VALUE); }
}

<PROPERTY_NAME> {  /* # Property:	Grapheme_Cluster_Break */
  [^ \t\r\n]+   { propertyName = yytext(); }

  {Spaces} {NL} { yybegin(YYINITIAL); }
}

<DEFAULT_PROPERTY_VALUE> {  /* # @missing: 0000..10FFFF; Other */
  /* Assumption: only one default property value is specified, 
   * so the specified interval can be ignored. 
   */
  {Hex} ".." {Hex} {ItemSeparator} { }
                     
  [^ \t\r\n]+ { defaultPropertyValue = yytext(); }
  
  {Spaces} {NL} { yybegin(YYINITIAL); }
}

<PROPERTY_VALUE> {
  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)*  {
                                        addInterval( start, end, yytext());
                                      }

  {Spaces} ("#" .*)? {NL} { yybegin(YYINITIAL); }
}

<YYINITIAL> {
  <PROPERTY_NAME> {
    <DEFAULT_PROPERTY_VALUE> {
      <PROPERTY_VALUE> {
        <<EOF>> { addPropertyValueIntervals(); return 0; }
      }
    }
  }
}
