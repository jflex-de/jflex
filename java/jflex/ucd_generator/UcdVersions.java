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
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import java.io.File;
import java.util.List;

public class UcdVersions {

  // version –> Map<UcdFileType, File>
  private final ImmutableSortedMap<String, ImmutableMap<UcdFileType, File>> versions;

  private UcdVersions(ImmutableSortedMap<String, ImmutableMap<UcdFileType, File>> versions) {
    this.versions = versions;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static UcdVersions of(
      String v1,
      ImmutableMap<UcdFileType, File> ucd1,
      String v2,
      ImmutableMap<UcdFileType, File> ucd2) {
    return builder().put(v1, ucd1).put(v2, ucd2).build();
  }

  public Iterable<String> versions() {
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

  @SuppressWarnings("unused") // Used in .vm
  public static ImmutableList<String> expandVersion(String version) {
    ImmutableList.Builder<String> expandedVersions = ImmutableList.builder();
    List<String> v = Splitter.on('.').splitToList(version);
    for (int i = 1; i <= v.size(); i++) {
      expandedVersions.add(Joiner.on('.').join(v.subList(0, i)));
    }
    return expandedVersions.build();
  }

  public ImmutableList<String> expandAllVersions() {
    ImmutableList.Builder<String> expandedVersions = ImmutableList.builder();
    for (String v : versions()) {
      expandedVersions.addAll(expandVersion(v));
    }
    return expandedVersions.build();
  }

  static class Builder {

    ImmutableSortedMap.Builder<String, ImmutableMap<UcdFileType, File>> versionsBuilder =
        ImmutableSortedMap.naturalOrder();

    public Builder put(String version, ImmutableMap<UcdFileType, File> ucdFiles) {
      versionsBuilder.put(version, ucdFiles);
      return this;
    }

    public UcdVersions build() {
      return new UcdVersions(versionsBuilder.build());
    }
  }
}
