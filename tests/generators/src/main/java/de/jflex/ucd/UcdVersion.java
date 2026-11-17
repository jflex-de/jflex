/*
 * Copyright (C) 2018-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import de.jflex.version.Version;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Describes a single Unicode version. */
public final class UcdVersion {

  private final Version version;
  private final Map<UcdFileType, File> files;

  private UcdVersion(Version version, Map<UcdFileType, File> files) {
    this.version = version;
    this.files = Collections.unmodifiableMap(new LinkedHashMap<>(files));
  }

  public Version version() {
    return version;
  }

  /** The files of this version, in the order in which they were added. */
  public Map<UcdFileType, File> files() {
    return files;
  }

  public File getFile(UcdFileType type) {
    return files().get(type);
  }

  public static Builder builder(String version) {
    return new Builder().setVersion(version);
  }

  public static Builder builder(Version version) {
    return new Builder().setVersion(version);
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

  public Builder toBuilder() {
    Builder builder = builder(version);
    builder.files.putAll(files);
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UcdVersion)) {
      return false;
    }
    UcdVersion other = (UcdVersion) o;
    return version.equals(other.version) && files.equals(other.files);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, files);
  }

  @Override
  public String toString() {
    return version().toString();
  }

  public static final class Builder {
    private Version version;
    private final Map<UcdFileType, File> files = new LinkedHashMap<>();

    private Builder() {}

    public Builder setVersion(Version version) {
      this.version = Objects.requireNonNull(version, "version");
      return this;
    }

    public Builder setVersion(String version) {
      return setVersion(new Version(version));
    }

    /** Adds a file. A second file of the same type is an error. */
    public Builder putFile(UcdFileType unicodeFileType, File file) {
      Objects.requireNonNull(unicodeFileType, "unicodeFileType");
      Objects.requireNonNull(file, "file");
      if (files.put(unicodeFileType, file) != null) {
        throw new IllegalArgumentException("Multiple files of type " + unicodeFileType);
      }
      return this;
    }

    public Version version() {
      return version;
    }

    public UcdVersion build() {
      return new UcdVersion(version, files);
    }
  }
}
