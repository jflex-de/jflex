/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.base;

import static de.jflex.util.javac.JavaPackageUtils.getPathForPackage;

import de.jflex.version.Version;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class UnicodeVersion {
  private final Version version;
  private final String underscoreVersion;
  private final String javaPackage;
  private final Path javaPackageDirectory;

  private UnicodeVersion(Version version) {
    this.version = Objects.requireNonNull(version, "version");
    this.underscoreVersion = version.underscoreVersion();
    this.javaPackage = "de.jflex.testcase.unicode.unicode_" + underscoreVersion;
    this.javaPackageDirectory = Paths.get(getPathForPackage(javaPackage));
  }

  /** Unicode version. */
  public Version version() {
    return version;
  }

  public String underscoreVersion() {
    return underscoreVersion;
  }

  public String javaPackage() {
    return javaPackage;
  }

  public Path javaPackageDirectory() {
    return javaPackageDirectory;
  }

  public static UnicodeVersion create(Version unicodeVersion) {
    return new UnicodeVersion(unicodeVersion);
  }

  public static UnicodeVersion create(String version) {
    return create(new Version(version));
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UnicodeVersion)) {
      return false;
    }
    UnicodeVersion other = (UnicodeVersion) o;
    return version.equals(other.version)
        && underscoreVersion.equals(other.underscoreVersion)
        && javaPackage.equals(other.javaPackage)
        && javaPackageDirectory.equals(other.javaPackageDirectory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, underscoreVersion, javaPackage, javaPackageDirectory);
  }

  @Override
  public String toString() {
    return "UnicodeVersion{version="
        + version
        + ", underscoreVersion="
        + underscoreVersion
        + ", javaPackage="
        + javaPackage
        + ", javaPackageDirectory="
        + javaPackageDirectory
        + "}";
  }
}
