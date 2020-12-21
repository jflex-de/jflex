package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.model.UnicodeData;
import de.jflex.ucd_generator.ucd.NamedCodepointRange;
import java.util.SortedSet;
import java.util.TreeSet;

/** Scans the common single-property Unicode.org data file format. */
public abstract class AbstractEnumeratedPropertyFileScanner {

  final UnicodeData unicodeData;

  final SortedSet<NamedCodepointRange> intervals =
      new TreeSet<>(NamedCodepointRange.START_COMPARATOR);

  String propertyName;
  String defaultPropertyValue;
  int start;
  int end;

  protected AbstractEnumeratedPropertyFileScanner(
      UnicodeData unicodeData, String defaultPropertyName, String defaultPropertyValue) {
    this.unicodeData = unicodeData;
    this.propertyName = defaultPropertyName;
    this.defaultPropertyValue = defaultPropertyValue;
  }

  public void addInterval(NamedCodepointRange interval) {
    intervals.add(interval);
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

  protected boolean accept(String propertyValue) {
    return true;
  }
}
