package jflex.ucd_generator.scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.CodepointRange;

/** Scans the common multiple binary property Unicode.org data file format. */
public abstract class AbstractBinaryPropertiesFileScanner {

  private final UnicodeData unicodeData;

  String propertyName;
  int start;
  int end;

  private final HashMap<String, SortedSet<CodepointRange>> properties = new HashMap<>();

  public AbstractBinaryPropertiesFileScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  public void addPropertyIntervals() {
    for (Map.Entry<String, SortedSet<CodepointRange>> property : properties.entrySet()) {
      String currentPropertyName = property.getKey();
      SortedSet<CodepointRange> intervals = property.getValue();
      int prevEnd = -1;
      int prevStart = -1;
      for (CodepointRange interval : intervals) {
        if (prevEnd == -1) {
          prevStart = interval.start();
        } else if (interval.start() > prevEnd + 1) {
          unicodeData.addPropertyInterval(currentPropertyName, prevStart, prevEnd);
          prevStart = interval.start();
        }
        prevEnd = interval.end();
      }
      // Add final interval
      unicodeData.addPropertyInterval(currentPropertyName, prevStart, prevEnd);
    }
    properties.clear();
  }

  public void addCurrentInterval() {
    SortedSet<CodepointRange> intervals =
        properties.computeIfAbsent(propertyName, k -> new TreeSet<>(CodepointRange.COMPARATOR));
    intervals.add(CodepointRange.create(start, end));
  }
}
