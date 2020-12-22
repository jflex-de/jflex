package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.model.UnicodeData;
import de.jflex.ucd_generator.ucd.NamedCodepointRange;

%%

%public
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
  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { String val = yytext();
                                         if (accept(val)) {
                                           addInterval(NamedCodepointRange.create(val, start, end));
                                         }
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
