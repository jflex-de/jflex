/*
 * Copyright (C) 2018 Google, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jflex.ucd_generator;

import static java.lang.Math.min;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Comparator;
import java.util.List;

public class UcdVersions {

  // version –> Map<UcdFileType, File>
  private final ImmutableSortedMap<String, UcdVersion> versions;

  private UcdVersions(ImmutableSortedMap<String, UcdVersion> versions) {
    this.versions = versions;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static UcdVersions of(
      String v1,
      UcdVersion.Builder ucd1,
      String v2,
      UcdVersion.Builder ucd2,
      String v3,
      UcdVersion.Builder ucd3,
      String v4,
      UcdVersion.Builder ucd4) {
    return builder().put(v1, ucd1).put(v2, ucd2).put(v3, ucd3).put(v4, ucd4).build();
  }

  public ImmutableSet<String> versions() {
    return versions.keySet();
  }

  public String getLastVersion() {
    return versions.lastKey();
  }

  @SuppressWarnings("unused") // Used in .vm
  public static String getClassNameForVersion(String version) {
    // TODO: This should be in emitter
    List<String> v = Splitter.on('.').splitToList(version);
    return "Unicode_" + Joiner.on('_').join(v.subList(0, min(2, v.size())));
  }

  /** Expands the version {@code x.y.z} into {@code x}, {@code x.y}, {@code x.y.z}. */
  @SuppressWarnings("unused") // Used in .vm
  public static ImmutableList<String> expandVersion(String version) {
    ImmutableList.Builder<String> expandedVersions = ImmutableList.builder();
    List<String> v = Splitter.on('.').splitToList(version);
    // Add the major version x if it is a x.0.z version
    if (v.size() == 1 || (v.size() > 1 && Objects.equal(v.get(1), "0"))) {
      expandedVersions.add(v.get(0));
    }
    for (int i = 2; i <= v.size(); i++) {
      String subVersion = Joiner.on('.').join(v.subList(0, i));
      expandedVersions.add(subVersion);
    }
    return expandedVersions.build();
  }

  /**
   * Expands all versions.
   *
   * @return the set of all versions, in decreasing order.
   */
  public ImmutableSortedSet<String> expandAllVersions() {
    ImmutableSortedSet.Builder<String> expandedVersions =
        ImmutableSortedSet.orderedBy(new VersionComparator());
    for (String v : versions()) {
      expandedVersions.addAll(expandVersion(v));
    }
    return expandedVersions.build();
  }

  static class Builder {

    ImmutableSortedMap.Builder<String, UcdVersion> versionsBuilder =
        ImmutableSortedMap.orderedBy(new VersionComparator());

    public Builder put(String version, UcdVersion.Builder ucdFiles) {
      versionsBuilder.put(version, ucdFiles.withVersion(version).build());
      return this;
    }

    public UcdVersions build() {
      return new UcdVersions(versionsBuilder.build());
    }
  }

  private static class VersionComparator implements Comparator<String> {

    @Override
    public int compare(String left, String right) {
      Version leftVersion = new Version(left);
      Version rightVersion = new Version(right);
      return leftVersion.compareTo(rightVersion);
    }
  }

  private static class Version implements Comparable<Version> {

    final int major;
    final int minor;
    final int patch;

    public Version(String version) {
      List<String> parts = Splitter.on(".").splitToList(version);
      this.major = Integer.parseInt(parts.get(0));
      this.minor = parts.size() > 1 ? Integer.parseInt(parts.get(1)) : -1;
      this.patch = parts.size() > 2 ? Integer.parseInt(parts.get(2)) : -1;
    }

    @Override
    public int compareTo(Version other) {
      if (this.major != other.major) {
        return this.major - other.major;
      }
      if (this.minor != other.minor) {
        return this.minor - other.minor;
      }
      return this.patch - other.patch;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Version)) {
        return false;
      }
      Version other = (Version) o;
      return this.major == other.major && this.minor == other.minor && this.patch == other.patch;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.major, this.minor, this.patch);
    }
  }
}
