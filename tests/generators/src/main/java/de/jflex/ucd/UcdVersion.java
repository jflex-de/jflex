/*
 * Copyright (C) 2018-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import de.jflex.version.Version;
import java.io.File;
import java.io.FileNotFoundException;

/** Describes a single Unicode version. */
@AutoValue
public abstract class UcdVersion {

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
      String name = new File(arg).getName();
      for (UcdFileType type : UcdFileType.values()) {
        // Unicode 4.1 and later use plain names, 1.0-4.0 use names such as Blocks-4.0.1.txt
        if (name.contains(type.toString())) {
          builder.putFile(type, findFile(arg));
        }
      }
      // Emoji is a single URL and uses a different naming convention
      if (name.equals("emoji-data.txt")) {
        builder.putFile(UcdFileType.Emoji, findFile(arg));
      }
    }
    return builder.build();
  }

  private static File findFile(String arg) throws FileNotFoundException {
    File file = new File(arg);
    if (file.exists()) {
      return file;
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
      filesBuilder().put(unicodeFileType, file);
      return this;
    }

    public abstract Version version();

    public abstract UcdVersion build();
  }
}
