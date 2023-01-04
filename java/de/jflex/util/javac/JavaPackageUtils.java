/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.util.javac;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public final class JavaPackageUtils {
  public static String getPathForClass(Class clazz) {
    return getPathForPackage(clazz.getPackage());
  }

  public static String getPathForPackage(Package targetPackage) {
    return getPathForPackage(targetPackage.getName());
  }

  public static String getPathForPackage(String packageName) {
    return packageName.replace('.', File.separatorChar);
  }

  private JavaPackageUtils() {}
}
