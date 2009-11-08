package jflex;

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
%public
%class ArchaicBlocksScanner
%ctorarg UnicodeVersion unicodeVersion

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_VALUE, COMMENT_LINE

%int
%function scan

%{
  UnicodeVersion unicodeVersion;
  SortedSet<NamedRange> intervals = new TreeSet<NamedRange>();
  String defaultPropertyValue = "No_Block";
  String propertyName = "Block";
  int start;
  int end;
  
  public void addPropertyValueIntervals() {
    int prevEnd = -1;
    int prevStart = -1;
    String prevValue = "";
    for (NamedRange interval : intervals) {
      if (interval.start > prevEnd + 1) {
        // Unassigned code points get the default property value, e.g. "Unknown"
        unicodeVersion.addInterval
          (propertyName, defaultPropertyValue, prevEnd + 1, interval.start - 1);
      }
      if (prevEnd == -1) {
        prevStart = interval.start;
        prevValue = interval.name;
      } else if (interval.start > prevEnd + 1 || ! interval.name.equals(prevValue)) {
        unicodeVersion.addInterval(propertyName, prevValue, prevStart, prevEnd);
        prevStart = interval.start;
        prevValue = interval.name;
      }
      prevEnd = interval.end;
    }

    // Add final default property value interval, if necessary
    if (prevEnd < unicodeVersion.maximumCodePoint) {
      unicodeVersion.addInterval(propertyName, defaultPropertyValue, 
                                 prevEnd + 1, unicodeVersion.maximumCodePoint);
    }
    
    // Add final named interval
    unicodeVersion.addInterval(propertyName, prevValue, prevStart, prevEnd);
  }
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
