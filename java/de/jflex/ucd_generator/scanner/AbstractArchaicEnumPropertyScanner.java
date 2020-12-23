package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.NamedCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.SortedSet;
import java.util.TreeSet;

public class AbstractArchaicEnumPropertyScanner {
  final UnicodeData unicodeData;
  final String propertyName;
  final String defaultPropertyValue;

  final SortedSet<NamedCodepointRange> intervals =
      new TreeSet<>(NamedCodepointRange.START_COMPARATOR);

  String propertyValue;
  int start;
  int end;

  public AbstractArchaicEnumPropertyScanner(
      UnicodeData unicodeData, String propertyName, String defaultPropertyValue) {
    this.unicodeData = unicodeData;
    this.propertyName = propertyName;
    this.defaultPropertyValue = defaultPropertyValue;
    this.propertyValue = defaultPropertyValue;
  }

  protected void addInterval(int start, int end, String text) {
    intervals.add(NamedCodepointRange.create(text, start, end));
  }

  public void addPropertyValueIntervals() {
    int prevEnd = -1;
    int prevStart = -1;
    String prevValue = "";
    for (NamedCodepointRange interval : intervals) {
      if (interval.start() > prevEnd + 1) {
        // Unassigned code points get the default property value, e.g. "Unknown"
        unicodeData.addEnumPropertyInterval(
            propertyName, defaultPropertyValue, prevEnd + 1, interval.start() - 1);
      }
      if (prevEnd == -1) {
        prevStart = interval.start();
        prevValue = interval.name();
      } else if (interval.start() > prevEnd + 1 || !interval.name().equals(prevValue)) {
        unicodeData.addEnumPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
        prevStart = interval.start();
        prevValue = interval.name();
      }
      prevEnd = interval.end();
    }

    // Add final default property value interval, if necessary
    if (prevEnd < unicodeData.maximumCodePoint()) {
      unicodeData.addEnumPropertyInterval(
          propertyName, defaultPropertyValue, prevEnd + 1, unicodeData.maximumCodePoint());
    }

    // Add final named interval
    unicodeData.addEnumPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
  }
}
