/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.util;

public class HexaUtils {
  private HexaUtils() {}

  public static Integer intFromHexa(String string) {
    if (string == null || string.isEmpty()) {
      return null;
    }
    return Integer.valueOf(string, 16);
  }
}
