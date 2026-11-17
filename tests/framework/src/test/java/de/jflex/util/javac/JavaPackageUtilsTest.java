/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.util.javac;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JavaPackageUtilsTest {

  @Test
  public void getPathForPackage() throws Exception {
    assertEquals(
        "de/jflex/util/javac", JavaPackageUtils.getPathForPackage(getClass().getPackage()));
  }
}
