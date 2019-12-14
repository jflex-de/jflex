package jflex.ucd_generator.scanner;

%%

%final
%public
%class PropertyAliasesScanner
%extends AbstractPropertyAliasesScanner
%ctorarg UnicodeData.Builder unicodeDataBuilder

%unicode
%eofclose

%state LONG_NAME, ADDITIONAL_ALIASES

%int
%function scan

%init{
  super(unicodeDataBuilder);
%init}

Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  {Spaces} ("#" .*)? {NL} { clear(); }

  // scf       ; Simple_Case_Folding         ; sfc
  // TODO(regisd) move call on normalize in addPropertyAliases
  [^ \t\r\n;]+ { aliases.add(PropertyNameNormalizer.normalize(yytext())); }
  
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
