package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.MutableCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.HashMap;
import java.util.Map;

public class AbstractArchaicPropListScanner {

  final UnicodeData unicodeData;
  HashMap<String, CodepointRangeSet.Builder> properties = new HashMap<>();

  String propertyName;
  int start;
  int end;

  public AbstractArchaicPropListScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
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
          unicodeData.addBinaryPropertyInterval(currentPropertyName, prevStart, prevEnd);
          prevStart = interval.start();
        }
        prevEnd = interval.end();
      }
      // Add final interval
      unicodeData.addBinaryPropertyInterval(currentPropertyName, prevStart, prevEnd);
    }
  }

  public void addCurrentInterval() {
    CodepointRangeSet.Builder intervals =
        properties.computeIfAbsent(propertyName, s -> CodepointRangeSet.builder());
    intervals.add(MutableCodepointRange.create(start, end));
  }
}
