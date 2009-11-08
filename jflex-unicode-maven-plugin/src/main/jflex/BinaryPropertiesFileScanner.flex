package jflex;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Scans the common multiple binary property Unicode.org data file format,
 * populating unicodeVersion.propertyValueIntervals.
 */
%%

%final
%public
%class BinaryPropertiesFileScanner
%ctorarg UnicodeVersion unicodeVersion

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_NAME

%int
%function scan

%{
  UnicodeVersion unicodeVersion;
  Map<String, SortedSet<NamedRange>> properties 
    = new HashMap<String, SortedSet<NamedRange>>();
  String propertyName;
  int start;
  int end;
  
  public void addPropertyIntervals() {
    for (Map.Entry<String,SortedSet<NamedRange>> property : properties.entrySet()) {
      String currentPropertyName = property.getKey();
      SortedSet<NamedRange> intervals = property.getValue();
      int prevEnd = -1;
      int prevStart = -1;
      for (NamedRange interval : intervals) {
        if (prevEnd == -1) {
          prevStart = interval.start;
        } else if (interval.start > prevEnd + 1) {
          unicodeVersion.addInterval(currentPropertyName, prevStart, prevEnd);
          prevStart = interval.start;
        }
        prevEnd = interval.end;
      }
      // Add final interval
      unicodeVersion.addInterval(currentPropertyName, prevStart, prevEnd);
    }
  }
  
  public void addCurrentInterval() {
    SortedSet<NamedRange> intervals = properties.get(propertyName);
    if (null == intervals) {
      intervals = new TreeSet<NamedRange>();
      properties.put(propertyName, intervals);
    }
    intervals.add(new NamedRange(start, end));
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
  [^ \t\r\n]+   { propertyName = yytext(); }

  {Spaces} ("#" .*)? {NL} { addCurrentInterval(); yybegin(YYINITIAL); }
}

<YYINITIAL> {
  <PROPERTY_NAME> {
    <<EOF>> { addPropertyIntervals(); return 0; }
  }
}
