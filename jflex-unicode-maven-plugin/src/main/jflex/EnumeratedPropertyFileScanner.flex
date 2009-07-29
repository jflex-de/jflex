package jflex;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Scans the common single-property Unicode.org data file format, populating
 * unicodeVersion.propertyValueIntervals and 
 * unicodeVersion.usedPropertyValueAliases.
 */
%%

%final
%public
%class EnumeratedPropertyFileScanner
%ctorarg UnicodeVersion unicodeVersion
%ctorarg String defaultPropertyName
%ctorarg String defaultPropertyValue

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_NAME, PROPERTY_VALUE
%state COMMENT_LINE, DEFAULT_PROPERTY_VALUE

%int
%function scan

%{
  UnicodeVersion unicodeVersion;
  SortedSet<NamedRange> intervals = new TreeSet<NamedRange>();
  String defaultPropertyValue;
  String propertyName;
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
  this.propertyName = defaultPropertyName;
  this.defaultPropertyValue = defaultPropertyValue;
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
  [^ \t\r\n#;]+ (" " [^ \t\r\n#;]+)* { intervals.add(new NamedRange(start, end, yytext())); }

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
