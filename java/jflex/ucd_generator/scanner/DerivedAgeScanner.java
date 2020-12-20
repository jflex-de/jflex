package jflex.ucd_generator.scanner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import java.io.Reader;
import java.util.List;
import jflex.ucd_generator.model.UnicodeData;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.NamedCodepointRange;
import jflex.ucd_generator.ucd.Version;

/** Scanner for the {@code DerivedAge.txt} ucd file, that populates the UnicodeData intervals. */
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
    HashMultimap<Version, CodepointRange> ageRangesPerVersion = clusterCodePointRangesPerVersion();
    ImmutableList<Version> versions =
        ImmutableList.sortedCopyOf(Version.EXACT_VERSION_COMPARATOR, ageRangesPerVersion.keySet());
    // starting from 1, as  the first  version is a no-op
    for (int i = 1; i < versions.size(); i++) {
      Version version = versions.get(i);
      List<Version> prevVersions = versions.subList(0, i); // toIndex is exclusive
      // TODO(regisd) Since we include recursively, it should be enough to include N-1
      includeVersionsOnVersion(version, prevVersions, ageRangesPerVersion);
    }
    // TODO(regisd) Give the Unassigned Age property value to the absolute complement
    // of the highest version's range set.
  }

  private void includeVersionsOnVersion(
      Version version,
      List<Version> versionsToInclude,
      HashMultimap<Version, CodepointRange> ageRangesPerVersion) {
    for (Version v : versionsToInclude) {
      for (CodepointRange range : ageRangesPerVersion.get(v)) {
        unicodeData.addEnumPropertyInterval(
            propertyName, version.toString(), range.start(), range.end());
      }
    }
  }

  HashMultimap<Version, CodepointRange> clusterCodePointRangesPerVersion() {
    HashMultimap<Version, CodepointRange> ageRangesPerVersion =
        HashMultimap.create(/*expectedKeys=*/ 25, /*expectedValuesPerKey=*/ 128);
    for (NamedCodepointRange interval : intervals) {
      ageRangesPerVersion.put(new Version(interval.name()), interval.range());
    }
    return ageRangesPerVersion;
  }
}
