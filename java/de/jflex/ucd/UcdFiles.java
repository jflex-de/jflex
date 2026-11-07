/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import java.io.File;

/**
 * Locates downloaded Unicode Character Database files.
 *
 * <p>Maven puts these files into {@code <dir>/<release>/<file>}, where {@code <dir>} is the system
 * property {@code jflex.ucd.dir}. Without that property they are in the Bazel runfiles, under
 * repository names derived from {@code release} and {@code file}.
 */
public final class UcdFiles {

  private static final String UCD_DIR = System.getProperty("jflex.ucd.dir");

  /**
   * Returns a file from a release that is published as a single zip file.
   *
   * @param release the release, e.g. {@code ucd_7_0}
   * @param name the path inside the zip file, e.g. {@code auxiliary/WordBreakProperty.txt}
   */
  public static File zippedFile(String release, String name) {
    File parent = UCD_DIR == null ? new File(UcdVersion.EXTERNAL) : new File(UCD_DIR);
    return new File(new File(parent, release), name);
  }

  /**
   * Returns a file that is published as a single file, not inside a zip file.
   *
   * @param release the release, e.g. {@code ucd_4_0_1} or {@code emoji_5}
   * @param name the published file name, e.g. {@code Blocks-4.0.1.txt}
   */
  public static File singleFile(String release, String name) {
    if (UCD_DIR != null) {
      return new File(new File(UCD_DIR, release), name);
    }
    return bazelHttpFile(release + "_" + name.replace('.', '_').replace('-', '_'));
  }

  /** Returns the {@code DerivedAge.txt} that covers the releases before Unicode 3.2. */
  public static File derivedAge() {
    String release = "ucd_derived_age";
    return UCD_DIR == null ? bazelHttpFile(release) : singleFile(release, "DerivedAge.txt");
  }

  private static File bazelHttpFile(String repository) {
    return new File(new File(UcdVersion.EXTERNAL, repository), "file/downloaded");
  }

  private UcdFiles() {}
}
