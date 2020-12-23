package de.jflex.ucd_generator.scanner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.MutableCodepointRange;
import de.jflex.ucd_generator.ucd.NamedCodepointRange;
import de.jflex.ucd_generator.ucd.SurrogateUtils;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.version.Version;
import java.io.Reader;
import java.util.List;
import java.util.Set;

/** Scanner for the {@code DerivedAge.txt} ucd file, that populates the UnicodeData intervals. */
class DerivedAgeScanner extends EnumeratedPropertyFileScanner {

  final int maximumCodepoint;

  public DerivedAgeScanner(
      Reader in, UnicodeData unicodeData, String defaultPropertyName, int maximumCodepoint) {
    super(in, unicodeData, defaultPropertyName, "unassigned");
    this.maximumCodepoint = maximumCodepoint;
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

  @Override
  public void addInterval(int start, int end, String name) {
    if (acceptProperty(name) && acceptInterval(start, end)) {
      super.addInterval(start, end, name);
    }
  }

  protected boolean acceptProperty(String propertyValue) {
    // For age interval, the name is the version.
    Version version = new Version(propertyValue);
    // Only add interval for past versions.
    return Version.MAJOR_MINOR_COMPARATOR.compare(version, unicodeData.version()) <= 0;
  }

  /**
   * Returns true if the interval has expected codepoints.
   *
   * <p>The {@code DerivedAge.txt} may come from UNIDATA for old Unicode versions, and these
   * versions (particularly these versions) may not support the range.
   */
  private boolean acceptInterval(int start, int end) {
    return start <= maximumCodepoint;
  }

  void includeOlderVersions() {
    HashMultimap<Version, CodepointRange> ageRangesPerVersion = clusterCodePointRangesPerVersion();
    ImmutableList<Version> versions =
        ImmutableList.sortedCopyOf(Version.EXACT_VERSION_COMPARATOR, ageRangesPerVersion.keySet());
    // starting from 1, as  the first  version is a no-op
    for (int i = 1; i < versions.size(); i++) {
      Version version = versions.get(i);
      List<Version> prevVersions = versions.subList(0, i); // toIndex is exclusive
      includeVersionsOnVersion(version, prevVersions, ageRangesPerVersion);
    }

    // Version up to 3.1.x use the UNIDATA DerivedAge
    // addUnassignedAge(ageRangesPerVersion);
  }

  /**
   * Gives the Unassigned Age property value to the absolute complement of the highest version's
   * range set.
   */
  // TODO(regisd) Remove dead code
  // The jflex-unicode-maven-plugin used to do this but this not necessary with ucd_generator?
  @SuppressWarnings("unused")
  private void addUnassignedAge(HashMultimap<Version, CodepointRange> ageRangesPerVersion) {
    Version lastVersion =
        ImmutableList.sortedCopyOf(Version.EXACT_VERSION_COMPARATOR, ageRangesPerVersion.keySet())
            .reverse()
            .get(0);
    // TODO(regisd) Assert that lastVersion == unicodeData.version()
    Set<CodepointRange> highestVersionRanges = ageRangesPerVersion.get(lastVersion);
    CodepointRangeSet unassigned =
        CodepointRangeSet.builder()
            .add(MutableCodepointRange.create(0, unicodeData.maximumCodePoint()))
            .substractAll(highestVersionRanges)
            .substract(SurrogateUtils.SURROGATE_RANGE)
            .build();
    for (CodepointRange range : unassigned.ranges()) {
      unicodeData.addEnumPropertyInterval(
          propertyName, defaultPropertyValue, range.start(), range.end());
    }
  }

  private void includeVersionsOnVersion(
      Version version,
      List<Version> versionsToInclude,
      HashMultimap<Version, CodepointRange> ageRangesPerVersion) {
    for (Version v : versionsToInclude) {
      for (CodepointRange range : ageRangesPerVersion.get(v)) {
        ageRangesPerVersion.put(v, range);
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
