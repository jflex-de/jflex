/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_generator.emitter.unicode_version;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.ucd_generator.util.JavaStrings;
import org.junit.jupiter.api.Test;

/** Unit test for {@link UnicodeVersionEmitter}. */
public class UnicodeVersionEmitterTest {
  @Test
  public void escapedUTF16Char() {
    assertEquals("\\000", JavaStrings.escapedUTF16Char(0));
    assertEquals("\\t", JavaStrings.escapedUTF16Char('\t'));
    assertEquals("\\u0061", JavaStrings.escapedUTF16Char('a'));
    assertEquals(
        "\\ud800\\udc0d", JavaStrings.escapedUTF16Char(Character.toCodePoint('\ud800', '\udc0d')));
  }
}
