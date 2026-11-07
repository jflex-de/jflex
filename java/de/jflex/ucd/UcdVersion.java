/*
 * Copyright (C) 2018-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
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
      // Bazel stores a single download as <repository>/file/downloaded, so only the path
      // shows the unicode data file type.
      String baseName = new File(arg).getName();
      String name = baseName.equals("downloaded") ? arg : baseName;
      for (UcdFileType type : UcdFileType.values()) {
        // Unicode 4.1 and later use plain names, 1.0-4.0 use names such as Blocks-4.0.1.txt
        if (name.contains(type.toString())) {
          builder.putFile(type, findFile(arg));
        }
      }
      // Emoji is a single URL and uses a different naming convention
      if (name.equals("emoji-data.txt") || name.contains("emoji_data_txt")) {
        builder.putFile(UcdFileType.Emoji, findFile(arg));
      }
      // Hack for the UNIDATA DerivedAge.txt
      if (name.contains("ucd_derived_age")) {
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

  /**
   * Given a path that contains "external", returns only the path after "external/" if it is a
   * relative path.
   */
  private static File findExternalPath(File file) {
    if (file.isAbsolute()) {
      // Bazel paths are relative. Bail out if it is absolute.
      return file;
    }
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
