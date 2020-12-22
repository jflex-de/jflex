package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.UnicodeData;

%%

%final
%class BinaryPropertiesFileScanner
%extends AbstractBinaryPropertiesFileScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_NAME

%int
%function scan


%init{
  super(unicodeData);
%init}

Hex = [0-9A-Fa-f]{4,6}
Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  {Spaces} ("#" .*)? {NL} { }

  {Hex} { start = Integer.parseInt(yytext(), 16); yybegin(BEGIN_RANGE); }
}

<BEGIN_RANGE> {
  ".." { yybegin(END_RANGE); }
  
  {ItemSeparator} { end = start; yybegin(PROPERTY_NAME); }
}

<END_RANGE> {
  {Hex} { end = Integer.parseInt(yytext(), 16); }
  
  {ItemSeparator} { yybegin(PROPERTY_NAME); }
}

<PROPERTY_NAME> {
  [^ \t\r\n#]+   { propertyName = yytext(); }

  {Spaces} ("#" .*)? {NL} { addCurrentInterval(); yybegin(YYINITIAL); }
}

<YYINITIAL> {
  <PROPERTY_NAME> {
    <<EOF>> { addPropertyIntervals(); return 0; }
  }
}
