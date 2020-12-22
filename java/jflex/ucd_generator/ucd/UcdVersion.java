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

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import java.io.File;

/** Describes a single Unicode version. */
@AutoValue
public abstract class UcdVersion {

  public static final String EXTERNAL = "external";

  public abstract Version version();

  public abstract ImmutableMap<UcdFileType, File> files();

  public File getFile(UcdFileType type) {
    return files().get(type);
  }

  public static Builder builder(String version) {
    return new AutoValue_UcdVersion.Builder().setVersion(version);
  }

  public static Builder builder(Version version) {
    return new AutoValue_UcdVersion.Builder().setVersion(version);
  }

  public abstract Builder toBuilder();

  @Override
  public final String toString() {
    return version().toString();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setVersion(Version version);

    public Builder setVersion(String version) {
      return setVersion(new Version(version));
    }

    abstract ImmutableMap.Builder<UcdFileType, File> filesBuilder();

    public Builder putFile(UcdFileType unicodeFileType, File file) {
      filesBuilder().put(unicodeFileType, findExternalPath(file));
      return this;
    }

    public abstract Version version();

    public abstract UcdVersion build();
  }

  /** Given a path that contains "external", returns only the path after "external/". */
  private static File findExternalPath(File file) {
    File externalDir = getExternalRootDirectory(file);
    if (externalDir != null) {
      return new File(file.getPath().substring(externalDir.getPath().length() - EXTERNAL.length()));
    }
    return file;
  }

  private static File getExternalRootDirectory(File file) {
    for (File parent = file.getParentFile(); parent != null; parent = parent.getParentFile()) {
      if (EXTERNAL.equals(parent.getName())) {
        return parent;
      }
    }
    return null;
  }
}
