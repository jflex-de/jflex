package jflex.ucd_generator.scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import jflex.ucd_generator.ucd.CodepointRange;

/** Scans the common multiple binary property Unicode.org data file format. */
public class AbstractBinaryPropertiesFileScanner {

  private final UnicodeData.Builder unicodeDataBuilder;

  String propertyName;
  int start;
  int end;

  private HashMap<String, SortedSet<CodepointRange>> properties = new HashMap<>();

  public AbstractBinaryPropertiesFileScanner(UnicodeData.Builder unicodeDataBuilder) {
    this.unicodeDataBuilder = unicodeDataBuilder;
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
          unicodeDataBuilder.addPropertyInterval(currentPropertyName, prevStart, prevEnd);
          prevStart = interval.start();
        }
        prevEnd = interval.end();
      }
      // Add final interval
      unicodeDataBuilder.addPropertyInterval(currentPropertyName, prevStart, prevEnd);
    }
  }

  public void addCurrentInterval() {
    SortedSet<CodepointRange> intervals =
        properties.computeIfAbsent(
            propertyName, k -> new TreeSet<>(CodepointRange.START_COMPARATOR));
    intervals.add(CodepointRange.create(start, end));
  }
}
