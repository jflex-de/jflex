/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import static de.jflex.ucd.SurrogateUtils.containsSurrogate;
import static de.jflex.ucd.SurrogateUtils.isSurrogateProperty;
import static de.jflex.ucd.SurrogateUtils.removeSurrogates;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/** Test for {@link SurrogateUtils}. */
public class SurrogateUtilsTest {
  @Test
  public void isSurrogate_not() {
    assertFalse(isSurrogateProperty("block=generalcategory"));
  }

  @Test
  public void isSurrogate_sc() {
    assertTrue(isSurrogateProperty("cs"));
    assertFalse(isSurrogateProperty("csxxx"));
    assertFalse(isSurrogateProperty("xxxcs"));
  }

  @Test
  public void isSurrogate_surrogate() {
    assertTrue(isSurrogateProperty("block=surrogate"));
    assertTrue(isSurrogateProperty("block=hightprivateusesurrogates"));
  }

  @Test
  public void removeSurrogate_surrogates() {
    assertTrue(removeSurrogates(0xd83f, 0xdffe).isEmpty());
  }

  @Test
  public void removeSurrogate_notSurrogates() {
    assertEquals(
        Arrays.asList(CodepointRange.create(0x0000, 0x01f5)), removeSurrogates(0x0000, 0x01f5));
  }

  @Test
  public void containsSurrogates_before() {
    assertFalse(containsSurrogate(CodepointRange.create(0x0000, 0x01f5)));
  }

  @Test
  public void containsSurrogates_within() {
    assertTrue(containsSurrogate(CodepointRange.create(0xD801, 0xD802)));
    assertTrue(containsSurrogate(CodepointRange.create(0xd83f, 0xdffe)));
  }

  @Test
  public void containsSurrogates_endOverlaps() {
    assertTrue(containsSurrogate(CodepointRange.create(0x0000, 0xD802)));
  }

  @Test
  public void containsSurrogates_after() {
    assertFalse(containsSurrogate(CodepointRange.create(0xE0000, 0xE0001)));
    assertFalse(containsSurrogate(CodepointRange.create(0xE0000, 0xF8FF)));
  }

  @Test
  public void containsSurrogates_larger() {
    assertTrue(containsSurrogate(CodepointRange.create(0x20ad, 0xfffb)));
  }
}
