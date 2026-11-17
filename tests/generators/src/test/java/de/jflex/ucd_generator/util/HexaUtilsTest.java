/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Test for {@link HexaUtils}. */
public class HexaUtilsTest {
  @Test
  public void intFromHexa_int() {
    assertEquals(255, HexaUtils.intFromHexa("FF"));
  }

  @Test
  public void intFromHexa_empty() {
    assertNull(HexaUtils.intFromHexa(""));
  }

  @Test
  public void intFromHexa_other() {
    assertThrows(IllegalArgumentException.class, () -> HexaUtils.intFromHexa("wtf"));
  }
}
