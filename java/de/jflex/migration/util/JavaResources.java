/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaResources {
  private JavaResources() {}

  public static InputStreamReader readResource(String resourceName) {
    InputStream resourceAsStream =
        checkNotNull(
            ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName),
            "Null resource content for " + resourceName);
    return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
  }
}
