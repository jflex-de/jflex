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

import com.google.common.base.Splitter;
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
    String major = v.get(0);
    String minor = v.get(1);
    return String.format("Unicode_%s_%s", major, minor);
  }

  @SuppressWarnings("unused") // Used in .vm
  public static String getMajorVersion(String version) {
    return version.substring(0, version.indexOf('.'));
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
