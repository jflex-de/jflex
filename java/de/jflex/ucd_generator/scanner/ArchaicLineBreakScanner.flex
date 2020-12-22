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
%extends AbstractArchaicLineBreakScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_VALUE, COMMENT_LINE, TWO_LINE_RANGE

%int
%function scan

%{
%}

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

  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { intervals.add(new NamedRange(start, end, yytext())); }

  {ItemSeparator} .* {NL} { yybegin(YYINITIAL); }
}

<TWO_LINE_RANGE> {
  {ItemSeparator} "<" [^,>]+ ", First>" {Spaces} {NL} { }
  
  {Hex} { end = Integer.parseInt(yytext(), 16); }
  
  {ItemSeparator} [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { /* Ignore second property value mention */ }
  
  {ItemSeparator} "<" [^,>]+ ", Last>" {Spaces} {NL} { intervals.add(new NamedRange(start, end, propertyValue));
                                                       yybegin(YYINITIAL);
                                                     }
}

<YYINITIAL> {
  <PROPERTY_VALUE> {
    <<EOF>> { addPropertyValueIntervals(); return 0; }
  }
}
