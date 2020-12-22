package de.jflex.ucd_generator.scanner;

public class AbstractArchaicLineBreakScanner {

  UnicodeVersion unicodeVersion;
  SortedSet<NamedRange> intervals = new TreeSet<NamedRange>();
  String defaultPropertyValue = "XX";
  String propertyName = "Line_Break";
  String propertyValue;
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
}
