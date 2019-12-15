package jflex.ucd_generator.scanner;

import java.util.SortedSet;
import java.util.TreeSet;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.NamedCodepointRange;

/** Scans the common single-property Unicode.org data file format. */
public abstract class AbstractEnumeratedPropertyFileScanner {

  final UnicodeData.Builder unicodeDataBuilder;

  final SortedSet<NamedCodepointRange> intervals =
      new TreeSet<>(NamedCodepointRange.START_COMPARATOR);

  String propertyName;
  String defaultPropertyValue;
  int start;
  int end;

  protected AbstractEnumeratedPropertyFileScanner(UnicodeData.Builder unicodeDataBuilder) {
    this.unicodeDataBuilder = unicodeDataBuilder;
  }

  public void addPropertyValueIntervals() {
    int prevEnd = -1;
    int prevStart = -1;
    String prevValue = "";
    for (NamedCodepointRange interval : intervals) {
      if (interval.start() > prevEnd + 1) {
        // Unassigned code points get the default property value, e.g. "Unknown"
        unicodeDataBuilder.addPropertyInterval(
            propertyName, defaultPropertyValue, prevEnd + 1, interval.start() - 1);
      }
      if (prevEnd == -1) {
        prevStart = interval.start();
        prevValue = interval.name();
      } else if (interval.start() > prevEnd + 1 || !interval.name().equals(prevValue)) {
        unicodeDataBuilder.addPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
        prevStart = interval.start();
        prevValue = interval.name();
      }
      prevEnd = interval.end();
    }

    // Add final default property value interval, if necessary
    if (prevEnd < unicodeDataBuilder.maximumCodePoint()) {
      unicodeDataBuilder.addPropertyInterval(
          propertyName, defaultPropertyValue, prevEnd + 1, unicodeDataBuilder.maximumCodePoint());
    }

    // Add final named interval
    unicodeDataBuilder.addPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
  }

  protected boolean accept(String propertyValue) {
    return true;
  }
}
