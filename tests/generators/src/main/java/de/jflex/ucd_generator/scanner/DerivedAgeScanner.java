/*
 * Copyright (C) 2013-2017 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017 Bruno P. Kinoshita
 * Copyright (C) 2017-2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.version.Version;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    Map<Version, Set<CodepointRange>> ageRangesPerVersion = clusterCodePointRangesPerVersion();
    List<Version> versions = new ArrayList<>(ageRangesPerVersion.keySet());
    versions.sort(Version.EXACT_VERSION_COMPARATOR);
    // starting from 1, as  the first  version is a no-op
    for (int i = 1; i < versions.size(); i++) {
      Version version = versions.get(i);
      List<Version> prevVersions = versions.subList(0, i); // toIndex is exclusive
      includeVersionsOnVersion(version, prevVersions, ageRangesPerVersion);
    }
  }

  private void includeVersionsOnVersion(
      Version version,
      List<Version> versionsToInclude,
      Map<Version, Set<CodepointRange>> ageRangesPerVersion) {
    for (Version v : versionsToInclude) {
      for (CodepointRange range : ageRangesPerVersion.getOrDefault(v, Collections.emptySet())) {
        unicodeData.addEnumPropertyInterval(
            propertyName, version.toString(), range.start(), range.end());
      }
    }
  }

  Map<Version, Set<CodepointRange>> clusterCodePointRangesPerVersion() {
    Map<Version, Set<CodepointRange>> ageRangesPerVersion = new HashMap<>();
    for (NamedCodepointRange<String> interval : intervals) {
      ageRangesPerVersion
          .computeIfAbsent(new Version(interval.name()), k -> new HashSet<>())
          .add(interval.range());
    }
    return ageRangesPerVersion;
  }
}
