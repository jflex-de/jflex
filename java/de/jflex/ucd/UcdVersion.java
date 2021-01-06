/*
 * Copyright (C) 2018-2020 Google, LLC.
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
package de.jflex.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.version.Version;
import java.io.File;
import java.io.FileNotFoundException;

/** Describes a single Unicode version. */
@AutoValue
public abstract class UcdVersion {

  private static final String BAZEL_PREFIX = JavaPackageUtils.getPathForClass(UcdVersion.class);

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

  public static UcdVersion findUcdFiles(Version version, Iterable<String> argv)
      throws FileNotFoundException {
    Builder builder = builder(version);
    for (String arg : argv) {
      for (UcdFileType type : UcdFileType.values()) {
        // From Unicode 4.1, a zip contains all files, which can just be found by name
        if (arg.endsWith(type.name() + ".txt")) {
          builder.putFile(type, findFile(arg));
        } else if (type == UcdFileType.Emoji && arg.contains("emoji_data_txt")) {
          // Emoji is a single URL, hence uses a different naming convention
          builder.putFile(UcdFileType.Emoji, findFile(arg));
        } else if (arg.contains(type.toString())) {
          // similarly Unicode 1.0-4.0 is e.g.
          // external/ucd_4_0_1_Blocks_4_0_1_txt/file/downloaded
          builder.putFile(type, findFile(arg));
        }
      }
      // Hack for the UNIDATA DerivedAge.txt
      if (arg.contains("ucd_derived_age")) {
        builder.putFile(UcdFileType.DerivedAge, findFile(arg));
      }
    }
    return builder.build();
  }

  private static File findFile(String arg) throws FileNotFoundException {
    File file = new File(arg);
    if (file.exists()) {
      return file;
    }
    // File downloaded by Bazel in the external dir
    File externalFile = new File(BAZEL_PREFIX, arg);
    if (file.exists()) {
      return externalFile;
    }
    throw new FileNotFoundException(arg);
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
