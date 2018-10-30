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

import com.google.common.collect.ImmutableMap;
import java.io.File;

public class UcdVersion {

  final String version;
  final ImmutableMap<UcdFileType, File> files;

  UcdVersion(String version, ImmutableMap<UcdFileType, File> files) {
    this.version = version;
    this.files = files;
  }

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    ImmutableMap.Builder<UcdFileType, File> files = ImmutableMap.builder();
    private String version;

    Builder withVersion(String version) {
      this.version = version;
      return this;
    }

    Builder putFile(UcdFileType unicodeFileType, File file) {
      files.put(unicodeFileType, file);
      return this;
    }

    public UcdVersion build() {
      return new UcdVersion(version, files.build());
    }
  }
}
