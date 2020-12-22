package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.NamedCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.SortedSet;
import java.util.TreeSet;

public class AbstractArchaicBlocksScanner {
  final UnicodeData unicodeVersion;

  final SortedSet<NamedCodepointRange> intervals =
      new TreeSet<>(NamedCodepointRange.START_COMPARATOR);

  String defaultPropertyValue = "No_Block";
  String propertyName = "Block";

  int start;
  int end;

  public AbstractArchaicBlocksScanner(UnicodeData unicodeVersion) {
    this.unicodeVersion = unicodeVersion;
  }

  public void addPropertyValueIntervals() {
    int prevEnd = -1;
    int prevStart = -1;
    String prevValue = "";
    for (NamedCodepointRange interval : intervals) {
      if (interval.start() > prevEnd + 1) {
        // Unassigned code points get the default property value, e.g. "Unknown"
        unicodeVersion.addEnumPropertyInterval(
            propertyName, defaultPropertyValue, prevEnd + 1, interval.start() - 1);
      }
      if (prevEnd == -1) {
        prevStart = interval.start();
        prevValue = interval.name();
      } else if (interval.start() > prevEnd + 1 || ! interval.name().equals(prevValue)) {
        unicodeVersion.addEnumPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
        prevStart = interval.start();
        prevValue = interval.name();
      }
      prevEnd = interval.end();
    }

    // Add final default property value interval, if necessary
    if (prevEnd < unicodeVersion.maximumCodePoint()) {
      unicodeVersion.addEnumPropertyInterval(propertyName, defaultPropertyValue,
          prevEnd + 1, unicodeVersion.maximumCodePoint());
    }

    // Add final named interval
    unicodeVersion.addEnumPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
  }
}
