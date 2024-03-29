/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import java.util.HashSet;
import java.util.Set;
import de.jflex.ucd_generator.ucd.PropertyNames;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.version.Version;

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
  [0-9]+ { aliases.add(PropertyNames.normalize(yytext())); }

  {ItemSeparator} { yybegin(ALIAS); }
}

// Canonical general category property values are the short forms 
<GENERAL_CATEGORY_PROPERTY_VALUE> {
  [^ \t\r\n;#]+ { propertyValue = yytext(); }
  
  {ItemSeparator} { yybegin(GENERAL_CATEGORY_ALIAS); }
}

// Long form general category property values are aliases
<GENERAL_CATEGORY_ALIAS> {
  [^ \t\r\n;#]+ { aliases.add(PropertyNames.normalize(yytext())); }
  
  {ItemSeparator} { yybegin(ADDITIONAL_ALIASES); }
}

<ALIAS> {
  [Nn] "/" [Aa] { }
  
  [^ \t\r\n;#]+ { aliases.add(PropertyNames.normalize(yytext())); }
  
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
  [^ \t\r\n;#]+ { aliases.add(PropertyNames.normalize(yytext())); }
  
  {ItemSeparator} { }
}
