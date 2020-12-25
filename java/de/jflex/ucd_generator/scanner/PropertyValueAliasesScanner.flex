/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
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

import java.util.HashSet;
import java.util.Set;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.ucd.UcdVersion;
import de.jflex.version.Version;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;

/**
 * Scans the PropertyValueAliases(-X.X.X).txt Unicode.org data file format,
 * populating unicodeVersion.allPropertyValueAliases.
 */
%%

%final
%class PropertyValueAliasesScanner
%extends AbstractPropertyValueAliasesScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state GENERAL_CATEGORY_PROPERTY_VALUE, GENERAL_CATEGORY_ALIAS
%state CCC, PROPERTY_VALUE, ALIAS, NO_ALIAS, ADDITIONAL_ALIASES 

%int
%function scan

%init{
  super(unicodeData);
%init}

Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

// Ext; N ; No  ; F ; False
// Ext; Y ; Yes ; T ; True
BinaryValueAliases = {ItemSeparator} "N" {ItemSeparator} "No" {ItemSeparator} "F" {ItemSeparator} "False" |
                     {ItemSeparator} "Y" {ItemSeparator} "Yes" {ItemSeparator} "T" {ItemSeparator} "True"

%%

<YYINITIAL> {
  // ccc;   0; NR   ; Not_Reordered
  "ccc" {ItemSeparator} { propertyAlias = "ccc"; 
                          yybegin(CCC);
                        }

  // gc ; C   ; Other       # Cc | Cf | Cn | Co | Cs
  "gc" {ItemSeparator} { propertyAlias = "gc";
                         yybegin(GENERAL_CATEGORY_PROPERTY_VALUE);
                       }
                       
  // Ext; N ; No  ; F ; False
  // Ext; Y ; Yes ; T ; True
  [^ \t\r\n;]+ {BinaryValueAliases} { /* Skip binary value aliases */ }

  // bc ; AL        ; Arabic_Letter
  // blk; n/a       ; Yijing_Hexagram_Symbols
  // dt ; Can       ; Canonical                        ; can
  [^ \t\r\n;]+ { propertyAlias = yytext(); }

  {ItemSeparator} { yybegin(ALIAS); }
  
  {Spaces} ("#" .*)? {NL}  { aliases.clear(); }
}

<CCC> {
  [0-9]+ { aliases.add(PropertyNameNormalizer.normalize(yytext())); }

  {ItemSeparator} { yybegin(ALIAS); }
}

// Canonical general category property values are the short forms 
<GENERAL_CATEGORY_PROPERTY_VALUE> {
  [^ \t\r\n;#]+ { propertyValue = yytext(); }
  
  {ItemSeparator} { yybegin(GENERAL_CATEGORY_ALIAS); }
}

// Long form general category property values are aliases
<GENERAL_CATEGORY_ALIAS> {
  [^ \t\r\n;#]+ { aliases.add(PropertyNameNormalizer.normalize(yytext())); }
  
  {ItemSeparator} { yybegin(ADDITIONAL_ALIASES); }
}

<ALIAS> {
  [Nn] "/" [Aa] { }
  
  [^ \t\r\n;#]+ { aliases.add(PropertyNameNormalizer.normalize(yytext())); }
  
  {ItemSeparator} { yybegin(PROPERTY_VALUE); }
}

<PROPERTY_VALUE> {
  [^ \t\r\n;#]+ { propertyValue = yytext(); }
            
  {ItemSeparator} { yybegin(ADDITIONAL_ALIASES); }

  <GENERAL_CATEGORY_ALIAS> {
    <ADDITIONAL_ALIASES> {
      {Spaces} ("#" .*)? {NL} { addPropertyValueAliases();
                                yybegin(YYINITIAL); 
                              }
    
      <<EOF>> { addPropertyValueAliases(); return 0; }
    }
  }
}

<ADDITIONAL_ALIASES> {
  [^ \t\r\n;#]+ { aliases.add(PropertyNameNormalizer.normalize(yytext())); }
  
  {ItemSeparator} { }
}
