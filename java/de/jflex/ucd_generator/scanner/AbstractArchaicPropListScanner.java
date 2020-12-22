package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.MutableCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.HashMap;
import java.util.Map;

public class AbstractArchaicPropListScanner {

  final UnicodeData unicodeVersion;
  HashMap<String, CodepointRangeSet.Builder> properties;

  String propertyName;
  int start;
  int end;

  public AbstractArchaicPropListScanner(UnicodeData unicodeVersion) {
    this.unicodeVersion = unicodeVersion;
  }

  public void addPropertyIntervals() {
    for (Map.Entry<String, CodepointRangeSet.Builder> property : properties.entrySet()) {
      String currentPropertyName = property.getKey();
      CodepointRangeSet intervals = property.getValue().build();
      int prevEnd = -1;
      int prevStart = -1;
      for (CodepointRange interval : intervals.ranges()) {
        if (prevEnd == -1) {
          prevStart = interval.start();
        } else if (interval.start() > prevEnd + 1) {
          unicodeVersion.addBinaryPropertyInterval(currentPropertyName, prevStart, prevEnd);
          prevStart = interval.start();
        }
        prevEnd = interval.end();
      }
      // Add final interval
      unicodeVersion.addBinaryPropertyInterval(currentPropertyName, prevStart, prevEnd);
    }
  }

  public void addCurrentInterval() {

    CodepointRangeSet.Builder intervals = properties.get(propertyName);
    if (intervals == null) {
      intervals = CodepointRangeSet.builder();
      properties.put(propertyName, intervals);
    }
    intervals.add(MutableCodepointRange.create(start, end));
  }
}
