package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Scans the Unicode.org data file format for Blocks-X.txt, from Unicode
 * versions 2.0, 2.1, and 3.0, populating unicodeVersion.propertyValueIntervals
 * and unicodeVersion.usedPropertyValueAliases.  From Unicode version 3.1 
 * onward, the Blocks(-X.X.X).txt file format changed to the common enumerated
 * properties format, which can be scanned using the grammar in 
 * EnumeratedPropertyFileScanner.flex.
 */
%%

%final
%class ArchaicBlocksScanner
%extends AbstractArchaicBlocksScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_VALUE, COMMENT_LINE

%int
%function scan

%init{
  this.unicodeVersion = unicodeVersion;
%init}

Hex = [0-9A-Fa-f]{4,6}
Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  {Spaces} "#" { yybegin(COMMENT_LINE); }
  
  {Spaces} {NL}? { }
  
  {Hex} { start = Integer.parseInt(yytext(), 16); yybegin(BEGIN_RANGE); }
}

<COMMENT_LINE> {
  .* {NL}? { yybegin(YYINITIAL); }
}

<BEGIN_RANGE> {
  {ItemSeparator} { yybegin(END_RANGE); }
}

<END_RANGE> {
  {Hex} { end = Integer.parseInt(yytext(), 16); }
  
  {ItemSeparator} { yybegin(PROPERTY_VALUE); }
}

<PROPERTY_VALUE> {
  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { intervals.add(new NamedRange(start, end, yytext())); }

  {Spaces} ("#" .*)? {NL} { yybegin(YYINITIAL); }
}

<YYINITIAL> {
  <PROPERTY_VALUE> {
    <<EOF>> { addPropertyValueIntervals(); return 0; }
  }
}
