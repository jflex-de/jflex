package jflex;

import java.util.HashSet;
import java.util.Set;

/**
 * Scans the PropertyAliases(-X.X.X).txt Unicode.org data file format, 
 * populating unicodeVersion.allPropertyAliases.
 */
%%

%final
%public
%class PropertyAliasesScanner
%ctorarg UnicodeVersion unicodeVersion

%unicode
%eofclose

%state LONG_NAME, ADDITIONAL_ALIASES

%int
%function scan

%{
  UnicodeVersion unicodeVersion;
  Set<String> aliases = new HashSet<String>();
  String longName;
  
  void addPropertyAliases() {
    String normalizedLongName = UnicodeVersion.normalize(longName);
    // Long names should resolve to themselves
    aliases.add(normalizedLongName);
    unicodeVersion.allPropertyAliases.put
      (normalizedLongName, new HashSet<String>(aliases));
    for (String alias : aliases) {
      unicodeVersion.propertyAlias2CanonicalName.put(alias, normalizedLongName);
    }
    longName = null;
    aliases.clear();
  }
%}

%init{
  this.unicodeVersion = unicodeVersion;
%init}

Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  {Spaces} ("#" .*)? {NL} { aliases.clear(); longName = null; }

  // scf       ; Simple_Case_Folding         ; sfc
  [^ \t\r\n;]+ { aliases.add(UnicodeVersion.normalize(yytext())); }
  
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
  [^ \t\r\n;#]+ { aliases.add(UnicodeVersion.normalize(yytext())); }
  
  {ItemSeparator} { }
}
