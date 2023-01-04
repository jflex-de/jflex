/*
 * Copyright (C) 2009 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.ucd.PropertyNames;

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
  [^ \t\r\n;#]+ { aliases.add(PropertyNames.normalize(yytext())); }
  
  {ItemSeparator} { }
}
