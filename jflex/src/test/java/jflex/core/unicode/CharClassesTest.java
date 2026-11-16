/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CharClasses}.
 *
 * @see CharClassesQuickcheck
 */
public class CharClassesTest {

  /** The classes of a 7 bit character set, as for {@code %7bit}. */
  private static CharClasses sevenBit() throws Exception {
    return new CharClasses(127, new UnicodeProperties());
  }

  /** The input set of the invariants is 0 to the maximum character code, not all of Unicode. */
  @Test
  public void invariants_smallCharSet() throws Exception {
    CharClasses classes = sevenBit();
    assertTrue(classes.invariants());

    classes.makeClass(IntCharSet.ofCharacterRange('a', 'z'));
    assertEquals(2, classes.getNumClasses());
    assertTrue(classes.invariants());
  }

  /** Characters above the maximum character code do not get a class. */
  @Test
  public void invariants_smallCharSet_classAboveMax() throws Exception {
    CharClasses classes = sevenBit();
    classes.makeClass(IntCharSet.ofCharacterRange('z', 0x10FFFF));
    assertEquals(2, classes.getNumClasses());
    assertTrue(classes.invariants());
    assertEquals(-1, classes.getClassCode(128));
  }
}
