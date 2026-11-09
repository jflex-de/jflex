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
 * property {@code jflex.ucd.dir}.
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
    return new File(new File(ucdDir(), release), name);
  }

  /**
   * Returns a file that is published as a single file, not inside a zip file.
   *
   * @param release the release, e.g. {@code ucd_4_0_1} or {@code emoji_5}
   * @param name the published file name, e.g. {@code Blocks-4.0.1.txt}
   */
  public static File singleFile(String release, String name) {
    return new File(new File(ucdDir(), release), name);
  }

  /** Returns the {@code DerivedAge.txt} that covers the releases before Unicode 3.2. */
  public static File derivedAge() {
    return singleFile("ucd_derived_age", "DerivedAge.txt");
  }

  private static File ucdDir() {
    if (UCD_DIR == null) {
      throw new IllegalStateException("System property jflex.ucd.dir is not set");
    }
    return new File(UCD_DIR);
  }

  private UcdFiles() {}
}
