/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.util.javac;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class JavaPackageUtilsTest {

  @Test
  public void getPathForPackage() throws Exception {
    assertThat(JavaPackageUtils.getPathForPackage(getClass().getPackage()))
        .isEqualTo("de/jflex/util/javac");
  }
}
