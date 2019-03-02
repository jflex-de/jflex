/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jflex.ucd_generator.ucd;

import static java.lang.Math.min;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import java.util.List;

/** A set of {@link UcdVersion}s. */
public class UcdVersions {

  // version –> Map<UcdFileType, File>
  private final ImmutableSortedMap<Version, UcdVersion> versions;

  private UcdVersions(ImmutableSortedMap<Version, UcdVersion> versions) {
    this.versions = versions;
  }

  public ImmutableSet<Version> versionSet() {
    return versions.keySet();
  }

  public List<String> versionsAsList() {
    ImmutableList.Builder<String> versionList = ImmutableList.<String>builder();
    for (Version v : versions.keySet()) {
      versionList.add(v.toString());
    }
    return versionList.build();
  }

  public UcdVersion get(Version version) {
    return versions.get(version);
  }

  public Version getLastVersion() {
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
  public static ImmutableList<String> expandVersion(Version version) {
    ImmutableList.Builder<String> expandedVersions = ImmutableList.builder();
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
    return expandedVersions.build();
  }

  public static ImmutableList<String> expandVersion(String version) {
    return expandVersion(new Version(version));
  }

  /**
   * Expands all versionSet.
   *
   * @return the set of all versionSet, in decreasing order.
   */
  public List<String> expandAllVersions() {
    ImmutableList.Builder<String> expandedVersions = ImmutableList.builder();
    for (Version v : versionSet()) {
      expandedVersions.addAll(expandVersion(v));
    }
    return expandedVersions.build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Version implements Comparable<Version> {

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

    @Override
    public String toString() {
      return makeString('.');
    }

    public String makeString(char sep) {
      StringBuilder v = new StringBuilder();
      v.append(major);
      if (minor != -1) {
        v.append(sep);
        v.append(minor);
      }
      if (minor != -1 && patch != -1) {
        v.append(sep);
        v.append(patch);
      }
      return v.toString();
    }
  }

  public static class Builder {
    ImmutableSortedMap.Builder<Version, UcdVersion> versions = ImmutableSortedMap.naturalOrder();

    private Builder put(Version version, UcdVersion.Builder ucdFiles) {
      versions.put(version, ucdFiles.build());
      return this;
    }

    public Builder put(String version, UcdVersion.Builder ucdFiles) {
      return put(new Version(version), ucdFiles);
    }

    public UcdVersions build() {
      return new UcdVersions(versions.build());
    }
  }
}
