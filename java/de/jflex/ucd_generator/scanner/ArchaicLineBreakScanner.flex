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
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Scans the Unicode.org data file format for LineBreak-X.txt for Unicode
 * version 3.0, populating unicodeVersion.propertyValueIntervals and
 * unicodeVersion.usedPropertyValueAliases.  From Unicode version 3.1 onward,
 * the LineBreak(-X.X.X).txt file format changed to the common enumerated
 * property format, which can be scanned using the grammar in 
 * EnumeratedPropertyFileScanner.flex.
 */
%%

%final
%class ArchaicLineBreakScanner
%extends AbstractArchaicEnumPropertyScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_VALUE, COMMENT_LINE, TWO_LINE_RANGE

%int
%function scan

%{
%}

%init{
  super(unicodeData, "Line_Break", "XX");
%init}

Hex = [0-9A-Fa-f]{4,6}
Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  {Spaces} "#" { yybegin(COMMENT_LINE); }
  
  {Spaces} {NL}? { }
  
  {Hex} { start = end = Integer.parseInt(yytext(), 16); }

  {ItemSeparator} { yybegin(PROPERTY_VALUE); }
}

<COMMENT_LINE> {
  .* {NL}? { yybegin(YYINITIAL); }
}

<PROPERTY_VALUE> {
  // AC00;ID;<Hangul Syllable, First>
  // D7A3;ID;<Hangul Syllable, Last>
  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* / {ItemSeparator} "<" [^>]+ ", First>" {Spaces} {NL} { propertyValue = yytext(); yybegin(TWO_LINE_RANGE); }

  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { addInterval(start, end, yytext()); }

  {ItemSeparator} .* {NL} { yybegin(YYINITIAL); }
}

<TWO_LINE_RANGE> {
  {ItemSeparator} "<" [^,>]+ ", First>" {Spaces} {NL} { }
  
  {Hex} { end = Integer.parseInt(yytext(), 16); }
  
  {ItemSeparator} [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { /* Ignore second property value mention */ }
  
  {ItemSeparator} "<" [^,>]+ ", Last>" {Spaces} {NL} { addInterval(start, end, propertyValue);
                                                       yybegin(YYINITIAL);
                                                     }
}

<YYINITIAL> {
  <PROPERTY_VALUE> {
    <<EOF>> { addPropertyValueIntervals(); return 0; }
  }
}
