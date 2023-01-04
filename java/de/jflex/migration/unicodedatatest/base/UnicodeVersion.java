/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.base;

import static de.jflex.util.javac.JavaPackageUtils.getPathForPackage;

import com.google.auto.value.AutoValue;
import de.jflex.version.Version;
import java.nio.file.Path;
import java.nio.file.Paths;

@AutoValue
public abstract class UnicodeVersion {
  /** Unicode version. */
  public abstract Version version();

  public abstract String underscoreVersion();

  public abstract String javaPackage();

  public abstract Path javaPackageDirectory();

  public static UnicodeVersion create(Version unicodeVersion) {
    String underscoreVersion = unicodeVersion.underscoreVersion();
    String javaPackage = "de.jflex.testcase.unicode.unicode_" + underscoreVersion;
    return new AutoValue_UnicodeVersion(
        unicodeVersion, underscoreVersion, javaPackage, Paths.get(getPathForPackage(javaPackage)));
  }

  public static UnicodeVersion create(String version) {
    return create(new Version(version));
  }
}
