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
import java.util.HashMap;
import java.util.Map;

/**
 * Scans the PropList-X.X.X.txt multiple binary property Unicode.org data file
 * format for Unicode versions 2.0, 2.1, and 3.0.
 * 
 * From Unicode version 3.1 onward, PropList(-X.X.X).txt uses the common binary
 * property Unicode.org data file format (see BinaryPropertiesFileScanner.flex
 * and the .java file generated from it).
 * 
 * unicodeVersion.propertyValueIntervals is populated.
 */
%%

%final
%class ArchaicPropListScanner
%extends AbstractArchaicPropListScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, IGNORE_REST_OF_LINE, PROPERTY_NAME

%int
%function scan

%init{
  super(unicodeData);
%init}

Hex = [0-9A-Fa-f]+
Spaces = [ \t]*
NL = \n | \r | \r\n

%%

// Property dump for: 0x10000001 (Zero-width)
// 200B..200F  (5 chars)
// FEFF

<YYINITIAL> {
  "Property dump for: 0x" {Hex} {Spaces} "(" { yybegin(PROPERTY_NAME); }

  {Hex} { start = Integer.parseInt(yytext(), 16); yybegin(BEGIN_RANGE); }
  
  . { yybegin(IGNORE_REST_OF_LINE); }
  
  {NL} { }
}

<BEGIN_RANGE> {
  ".." { yybegin(END_RANGE); }
  
  [^.\r\n]+ .* {NL} | {NL} { end = start; 
                             addCurrentInterval();
                             yybegin(YYINITIAL);
                           }
}

<END_RANGE> {
  {Hex} { end = Integer.parseInt(yytext(), 16);
          addCurrentInterval();
          yybegin(IGNORE_REST_OF_LINE);
        }
}

<IGNORE_REST_OF_LINE> .* {NL} { yybegin(YYINITIAL); }

<PROPERTY_NAME> {
  [^\r\n]+ ")" { propertyName = yytext().substring(0, yytext().length() - 1); 
                 yybegin(IGNORE_REST_OF_LINE);
               }
}

<YYINITIAL> {
  <IGNORE_REST_OF_LINE> {
    <BEGIN_RANGE> {
      <<EOF>> { addPropertyIntervals();  return 0; }
    }
  }
}