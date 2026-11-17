/*
 * Copyright (C) 2018-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.ucd;

import static java.lang.Math.min;

import de.jflex.ucd.UcdVersion;
import de.jflex.version.Version;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/** A set of {@link UcdVersion}s. */
public class UcdVersions {

  // version –> Map<UcdFileType, File>
  private final SortedMap<Version, UcdVersion> versions;

  private UcdVersions(SortedMap<Version, UcdVersion> versions) {
    this.versions = versions;
  }

  public Set<Version> versionSet() {
    return Collections.unmodifiableSet(versions.keySet());
  }

  public List<String> versionsAsList() {
    List<String> versionList = new ArrayList<>();
    for (Version v : versions.keySet()) {
      versionList.add(v.toString());
    }
    return Collections.unmodifiableList(versionList);
  }

  public UcdVersion get(Version version) {
    return versions.get(version);
  }

  public Version getLastVersion() {
    return versions.lastKey();
  }

  public static String getClassNameForVersion(String version) {
    List<String> v = Arrays.asList(version.split("\\.", -1));
    return "Unicode_" + String.join("_", v.subList(0, min(2, v.size())));
  }

  /** Expands the version {@code x.y.z} into {@code x}, {@code x.y}, {@code x.y.z}. */
  public static List<String> expandVersion(Version version) {
    List<String> expandedVersions = new ArrayList<>();
    // Add the major version x if it is a x.0.z version
    if (version.minor == -1 || (version.minor == 0 && version.patch != -1)) {
      expandedVersions.add(String.valueOf(version.major));
    }
    if (version.minor != -1) {
      expandedVersions.add(version.major + "." + version.minor);
    }
    if (version.minor != -1 && version.patch != -1) {
      expandedVersions.add(version.major + "." + version.minor + "." + version.patch);
    }
    return Collections.unmodifiableList(expandedVersions);
  }

  public static List<String> expandVersion(String version) {
    return expandVersion(new Version(version));
  }

  /**
   * Expands all versionSet.
   *
   * @return the set of all versionSet, in decreasing order.
   */
  public List<String> expandAllVersions() {
    List<String> expandedVersions = new ArrayList<>();
    for (Version v : versionSet()) {
      expandedVersions.addAll(expandVersion(v));
    }
    return Collections.unmodifiableList(expandedVersions);
  }

  public Builder toBuilder() {
    return builder().putAll(versions);
  }

  @Override
  public String toString() {
    return "[" + String.join(", ", versionsAsList()) + "]";
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final SortedMap<Version, UcdVersion> versions =
        new TreeMap<>(Version.EXACT_VERSION_COMPARATOR);

    public Builder putAll(Map<Version, UcdVersion> versions) {
      for (Map.Entry<Version, UcdVersion> entry : versions.entrySet()) {
        put(entry.getKey(), entry.getValue());
      }
      return this;
    }

    private Builder put(Version version, UcdVersion ucdFiles) {
      if (versions.put(version, ucdFiles) != null) {
        throw new IllegalArgumentException("Multiple entries with same key: " + version);
      }
      return this;
    }

    public Builder put(String version, UcdVersion ucdFiles) {
      return put(new Version(version), ucdFiles);
    }

    public UcdVersions build() {
      return new UcdVersions(Collections.unmodifiableSortedMap(new TreeMap<>(versions)));
    }
  }
}
