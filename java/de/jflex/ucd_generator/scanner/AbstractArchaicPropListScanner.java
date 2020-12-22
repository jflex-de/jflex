package de.jflex.ucd_generator.scanner;

public class AbstractArchaicPropListScanner {

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
}
