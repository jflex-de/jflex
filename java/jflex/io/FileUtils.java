/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.io;

import java.io.File;
import java.io.IOException;

public class FileUtils {

  /** Returns the path of {@code file} relative to {@code rootDirectory}. */
  public static String getRelativePath(File rootDirectory, File file) {
    try {
      String rootDir = rootDirectory.getCanonicalPath() + File.separator;
      String f = file.getCanonicalPath();
      if (f.startsWith(rootDir)) {
        return f.substring(rootDir.length());
      }
    } catch (IOException e) {
      // fall back to file.getPath()
    }
    return file.getPath();
  }

  /**
   * Replaces the {@code \} by a {@code /} in the path, because backslash is used as an escape
   * sequence in Java (e.g. {@code \u1234} is a unicode character.
   */
  public static String slashify(String path) {
    return path.replace('\\', '/');
  }

  private FileUtils() {} // utility class
}
