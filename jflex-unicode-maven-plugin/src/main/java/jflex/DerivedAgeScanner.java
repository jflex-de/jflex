package jflex;

import java.io.Reader;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scans the common single-property Unicode.org data file format
 * in DerivedAge.txt, populating unicodeVersion.propertyValueIntervals
 * for those property values representing Unicode versions less than
 * or equal to the Unicode version.
 */
public class DerivedAgeScanner extends EnumeratedPropertyFileScanner {
  private static final Pattern MAJOR_MINOR_VERSION_PATTERN
      = Pattern.compile("(\\d+)\\.(\\d+)");
  private static final Pattern AGE_PATTERN = Pattern.compile("(\\d+)\\.(\\d)");
  
  public DerivedAgeScanner(Reader in, UnicodeVersion unicodeVersion) {
    super(in, unicodeVersion, "Age", "Unassigned");
  }

  /**
   * Returns true if the given property value, which is a major.minor
   * Unicode version, is less than or equal to the Unicode version,
   * false otherwise.
   */
  @Override
  protected boolean accept(String propertyValue) {
    int major;
    int minor;
    Matcher matcher = MAJOR_MINOR_VERSION_PATTERN.matcher(propertyValue);
      if (matcher.matches()) {
        major = Integer.parseInt(matcher.group(1));
        minor = Integer.parseInt(matcher.group(2));
      } else {
        throw new RuntimeException("Malformed Age property value '" + propertyValue + "'");
      }
      return unicodeVersion.majorVersion > major
          || (  unicodeVersion.majorVersion == major
             && unicodeVersion.minorVersion >= minor);
  }

  /**
   * Specialized to make each version's code point ranges include all
   * older versions' code point ranges, and to handle overlapping
   * code point ranges.
   */
  @Override
  public void addPropertyValueIntervals() {
    SortedMap<String,NamedRangeSet> ageRangesPerVersion
        = new TreeMap<String,NamedRangeSet>();
      
    // Segregate ranges by version
    for (NamedRange interval : intervals) {
      String sortableVersion = getSortableVersion(interval.name);
      NamedRangeSet versionedAgeRanges 
          = ageRangesPerVersion.get(sortableVersion);
      if (null == versionedAgeRanges) {
        versionedAgeRanges = new NamedRangeSet();
        ageRangesPerVersion.put(sortableVersion, versionedAgeRanges);
      }
      versionedAgeRanges.add(new NamedRangeSet(interval));
    }
    
    String previousVersion = null;
    for (String version : ageRangesPerVersion.keySet()) {
      NamedRangeSet targetRanges = ageRangesPerVersion.get(version);
      if (null != previousVersion) {
        // Add previous version's ranges to the next version's
        targetRanges.add(ageRangesPerVersion.get(previousVersion));
      }
      previousVersion = version;
      String age = getAge(version);
      for (NamedRange range : targetRanges.getRanges()) {
        unicodeVersion.addInterval(propertyName, age, range.start, range.end);
      }
    }
      
    // Give the Unassigned Age property value to the absolute complement
    // of the highest version's range set.
    NamedRangeSet highestVersionRanges 
        = ageRangesPerVersion.get(ageRangesPerVersion.lastKey());
    NamedRangeSet unassignedRanges = new NamedRangeSet
        (new NamedRange(0, unicodeVersion.maximumCodePoint));
    unassignedRanges.sub(highestVersionRanges);
    for (NamedRange unassignedRange : unassignedRanges.getRanges()) {
      unicodeVersion.addInterval(propertyName, defaultPropertyValue, 
                                 unassignedRange.start, unassignedRange.end);
    }
  }

  /** Returns a two digit base 36 version string */
  private String getSortableVersion(String version) {
    Matcher matcher = AGE_PATTERN.matcher(version);
    if (matcher.matches()) {
      return Integer.toString(Integer.parseInt(matcher.group(1)), 36)
           + matcher.group(2);
    } 
    else {
      throw new RuntimeException
          ("Unparseable age property value: '" + version + "'");
    }
  }

  /**
   * Converts a two digit base 36 version string returned by
   * {@link #getSortableVersion} into a "\d+.\d" version string 
   */
  private String getAge(String sortableVersion) {
    return Integer.parseInt(sortableVersion.substring(0, 1), 36)
         + "." + sortableVersion.substring(1);
  }
}
