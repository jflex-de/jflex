package de.jflex.ucd_generator.scanner;

import com.google.common.collect.HashMultimap;
import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.Map;

public class AbstractArchaicPropListScanner {

  final UnicodeData unicodeData;

  /** Map of propName -> intervals */
  HashMultimap<String, CodepointRange> intervals = HashMultimap.create();

  String propertyName;
  int start;
  int end;

  public AbstractArchaicPropListScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  public void addPropertyIntervals() {
    for (Map.Entry<String, CodepointRange> property : intervals.entries()) {
      String currentPropertyName = property.getKey();
      CodepointRange interval = property.getValue();
      unicodeData.addBinaryPropertyInterval(currentPropertyName, interval);
    }
  }

  public void addCurrentInterval() {
    intervals.put(propertyName, CodepointRange.create(start, end));
  }
}
