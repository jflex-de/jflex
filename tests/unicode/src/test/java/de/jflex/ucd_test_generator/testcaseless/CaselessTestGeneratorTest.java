/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testcaseless;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.ucd.UcdFiles;
import de.jflex.ucd_test_generator.testcaseless.CaselessTestGenerator.Equivalences;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class CaselessTestGeneratorTest {

  /**
   * Test that caseless equivalences have been parsed.
   *
   * <pre>
   * {@code AC00;<Hangul Syllable,First>;Lo;0;L;;;;;N;;;;;}.
   * </pre>
   */
  @Test
  public void parseUnicodeData() throws Exception {
    Equivalences<Integer> equivalences =
        CaselessTestGenerator.parseUnicodeData(
            UcdFiles.zippedFile("ucd_7_0", "UnicodeData.txt").toPath());
    assertEquals(new HashSet<>(Arrays.asList(0x0041, 0x0061)), equivalences.get(0x0041));
    assertEquals(Collections.singleton(0xAC00), equivalences.get(0xAC00));
  }
}
