/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import de.jflex.version.Version;

public class Versions {
  public static final Version VERSION_1_1 = new Version(1, 1);
  public static final Version VERSION_2_0 = new Version(2, 0);
  public static final Version VERSION_3_0 = new Version(3, 0);
  public static final Version VERSION_3_1 = new Version(3, 1);
  public static final Version VERSION_3_2 = new Version(3, 2);
  public static final Version VERSION_4_1 = new Version(4, 1);
  public static final Version VERSION_5_0 = new Version(5, 0);
  public static final Version VERSION_6_0 = new Version(6, 0);
  public static final Version VERSION_8_0 = new Version(8, 0);

  public static int maxCodePoint(Version version) {
    boolean oldVersion = Version.MAJOR_MINOR_COMPARATOR.compare(version, VERSION_3_0) < 0;
    return oldVersion ? 0xFFFD : 0x10FFFF;
  }

  private Versions() {}
}
