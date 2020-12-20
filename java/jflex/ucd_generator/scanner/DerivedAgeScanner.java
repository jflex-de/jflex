package jflex.ucd_generator.scanner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import java.io.Reader;
import java.util.List;
import jflex.ucd_generator.model.UnicodeData;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.NamedCodepointRange;

/** Scanner for the {@code DerivedAge.txt} ucd file, that popualtes the UnicodeData intervals. */
public class DerivedAgeScanner extends EnumeratedPropertyFileScanner {

  public DerivedAgeScanner(Reader in, UnicodeData unicodeData, String defaultPropertyName) {
    super(in, unicodeData, defaultPropertyName, "unassigned");
  }

  /**
   * Overridden addPropertyValueIntervals() so that any unicode version includes all intervals from
   * the previous versions.
   */
  @Override
  public void addPropertyValueIntervals() {
    super.addPropertyValueIntervals();
    includeOlderVersions();
  }

  void includeOlderVersions() {
    HashMultimap<String, CodepointRange> ageRangesPerVersion = clusterCodePointRangesPerVersion();
    ImmutableList<String> versions = ImmutableList.sortedCopyOf(ageRangesPerVersion.keySet());
    // starting from 1, as  the first  version is a no-op
    for (int i = 1; i < versions.size(); i++) {
      String version = versions.get(i);
      List<String> prevVersions = versions.subList(0, i); // toIndex is exclusive
      includeVersionsOnVersion(version, prevVersions, ageRangesPerVersion);
    }
    // TODO(regisd) Give the Unassigned Age property value to the absolute complement
    // of the highest version's range set.
  }

  private void includeVersionsOnVersion(
      String version,
      List<String> versionsToInclude,
      HashMultimap<String, CodepointRange> ageRangesPerVersion) {
    for (String v : versionsToInclude) {
      for (CodepointRange range : ageRangesPerVersion.get(v)) {
        unicodeData.addEnumPropertyInterval(propertyName, version, range.start(), range.end());
      }
    }
  }

  HashMultimap<String, CodepointRange> clusterCodePointRangesPerVersion() {
    HashMultimap<String, CodepointRange> ageRangesPerVersion =
        HashMultimap.create(/*expectedKeys=*/ 25, /*expectedValuesPerKey=*/ 128);
    for (NamedCodepointRange interval : intervals) {
      ageRangesPerVersion.put(interval.name(), interval.range());
    }
    return ageRangesPerVersion;
  }
}
